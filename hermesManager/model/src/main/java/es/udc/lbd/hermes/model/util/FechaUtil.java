package es.udc.lbd.hermes.model.util;

import java.util.Calendar;

public class FechaUtil {
		
	/** Devuelve la semana siguiente (suma 7 dias) **/
	public static Calendar getSiguienteSemana(Calendar fecha2){
		//Clonamos la fecha
		Calendar fecha=Calendar.getInstance();
		fecha.setTimeInMillis(fecha2.getTimeInMillis());
		fecha.add(Calendar.DAY_OF_YEAR,7); 
		return fecha;
	}
	
	/** Devuelve una semana anterior a la fecha (resta 7 dias) **/
	public static Calendar getAnteriorSemana(Calendar fecha2){
		//Clonamos la fecha
		Calendar fecha=FechaUtil.getLimiteDiaInferior(fecha2);
		fecha.add(Calendar.DAY_OF_YEAR, -7); 
		return fecha;
	}

	/** Devuelve el mes anterior (día 1) */
	public static Calendar getAnteriorMes(Calendar fecha) {
		// Clonamos la fecha
		Calendar cal = FechaUtil.getLimiteDiaInferior(fecha);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal;
	}
	

	/** Devuelve el mes siguiente (día 1) */
	public static Calendar getSiguienteMes(Calendar fecha) {
		// Clonamos la fecha
		Calendar cal = FechaUtil.getLimiteDiaInferior(fecha);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal;
	}
	
	
	//Inicio del día, para comparaciones
	public static Calendar getLimiteDiaInferior(Calendar fecha2) {
		Calendar fecha=Calendar.getInstance();
		fecha.setTimeInMillis(fecha2.getTimeInMillis());
		fecha.set(Calendar.HOUR_OF_DAY, 0);
		fecha.set(Calendar.MINUTE,0);
		fecha.set(Calendar.SECOND,0);
		fecha.set(Calendar.MILLISECOND,0);
		return fecha;
	}

	/** Devuelve ese día a las 23:59:59:999
	 * Fin del día, para comparaciones
	 * @param fecha2
	 * @return
	 */
	public static Calendar getLimiteDiaSuperior(Calendar fecha2) {
		Calendar fecha=Calendar.getInstance();
		fecha.setTimeInMillis(fecha2.getTimeInMillis());
		fecha.set(Calendar.HOUR_OF_DAY, 23);
		fecha.set(Calendar.MINUTE,59);
		fecha.set(Calendar.SECOND,59);
		fecha.set(Calendar.MILLISECOND,999);
		return fecha;
	}
	
	
	//Inicio de la semana, para comparaciones
	public static Calendar getLimiteSemanaInferior(Calendar fecha2) {
		Calendar fecha=FechaUtil.getLimiteDiaInferior(fecha2);
		
		int diaSemana=fecha.get(Calendar.DAY_OF_WEEK);
		if (diaSemana==1) diaSemana=7;
		else diaSemana--;
		fecha.add(Calendar.DAY_OF_YEAR, -(diaSemana-1));
		fecha.set(Calendar.HOUR_OF_DAY, 0);
		fecha.set(Calendar.MINUTE,0);
		return fecha;
	}
	
	/**
	 * Devuelve el día 1 del mes de la fecha
	 * @return
	 */
	public static Calendar getInicioMes(Calendar fecha){
		Calendar inicioMes=Calendar.getInstance();
		inicioMes.setTimeInMillis(fecha.getTimeInMillis());
		inicioMes.set(Calendar.DAY_OF_MONTH, 1);
		return inicioMes;
	}
	
	 /**
	  * Devuelve el día 1 del año actual
	  * @return
	  */
	public static Calendar getInicioAnio(Calendar fecha){
		Calendar inicioAnio=Calendar.getInstance();
		inicioAnio.setTimeInMillis(fecha.getTimeInMillis());
		inicioAnio.set(Calendar.DAY_OF_MONTH, 1);
		inicioAnio.set(Calendar.MONTH, 1);
		return inicioAnio;
	}
	
	/**
	 * Devuelve el lunes de la siguiente semana a las 00:00
	 * @param fecha2
	 * @return
	 */
	public static Calendar getLimiteSemanaSuperior(Calendar fecha2) {
		Calendar fecha=getLimiteSemanaInferior(fecha2);
		fecha.add(Calendar.DAY_OF_YEAR,7);
		return fecha;
	}
	
	/**
	 * Devuelve el domingo de esa semana a las 23:59:59:999
	 * @param fecha
	 * @return
	 */
	public static Calendar getLimiteSemanaSuperiorDiaFin(Calendar fecha){
		Calendar fecha2=getLimiteSemanaInferior(fecha);
		fecha2.add(Calendar.DAY_OF_YEAR,7);
		fecha2.add(Calendar.MILLISECOND, -1);
		return fecha2;

	}
	
	public static Calendar getHoy() {
		return getLimiteDiaInferior(Calendar.getInstance());
	}
	
	public static boolean comprobarHoraMinuto(Integer hora, Integer minuto){
		
		if (hora != null && minuto != null){
			return  (0 <= hora && hora < 23) && ( 0 <= minuto && minuto <= 59);
		}else{
			//Un campo é nulo e outro non
			if (hora != null || minuto != null){
				return false;
			}
		}
		
		return true;
	}
	
}
