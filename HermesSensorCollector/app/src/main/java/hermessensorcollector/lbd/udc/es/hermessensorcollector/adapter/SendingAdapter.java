package hermessensorcollector.lbd.udc.es.hermessensorcollector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.R;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;

/**
 * Clase adapter con los items de los envios
 *
 * Created by Leticia on 25/04/2016.
 */
public class SendingAdapter  extends BaseAdapter {

    private ArrayList<TailSending> entradas;
    private List<TailSending> envios;

    public SendingAdapter(List<TailSending> envios){
        super();
        this.envios = envios;

        entradas = recuperarListaEnvios();
    }

    @Override
    public int getCount() {
        if (entradas!=null){
            return entradas.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (entradas!=null){
            return entradas.get(position);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_envio, null);
        }

        onEntrada (entradas.get(position), convertView, parent.getContext());

        return convertView;
    }

    /**
     * Recuperar la lista de envios a pintar en la lista
     *
     * @return ArrayList<FotoVista> Lista con las fotos a pintar en la interfaz
     */
    private ArrayList<TailSending> recuperarListaEnvios(){
        entradas = new ArrayList<TailSending>();

        for(int i=0;i<envios.size();i++){
            TailSending envio = envios.get(i);
            entradas.add(envio);
        }

        return entradas;
    }

    /** Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
     *
     * @param entrada La entrada que sera la asociada a la view. La entrada es del tipo del
     * paquete/handler
     * @param view View particular que contendra los datos del paquete/handler
     */
    public void onEntrada (Object entrada, View view, Context context){
        if (entrada != null) {
            TextView fechaEnvio = (TextView) view.findViewById(R.id.dateSending);
            if (fechaEnvio != null) {
                Date dateEnvio = ((TailSending) entrada).getDate();

                Locale locale = Utils.getLocale();
                String date = Utils.obtenerFechaSegunFormato(dateEnvio, Utils.FECHA_SQLITE, locale);

                fechaEnvio.setText(date);
            }

            TextView rutaEnvio = (TextView) view.findViewById(R.id.routeSending);
            if (rutaEnvio != null)
                rutaEnvio.setText(((TailSending) entrada).getRoutezip());


        }

    }
}
