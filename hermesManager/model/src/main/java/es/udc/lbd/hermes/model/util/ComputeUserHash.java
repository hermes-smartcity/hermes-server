package es.udc.lbd.hermes.model.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class ComputeUserHash {

	public static void main(String[] args) {
		String cadena = "luaces";
		String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("luaces " + hash);

		cadena = "luaces@lbd.org.es";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("luaces@lbd.org.es " + hash);

		cadena = "jorgeyago.ingeniero";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jorgeyago.ingeniero " + hash);

		cadena = "jorgeyago.ingeniero@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jorgeyago.ingeniero@gmail.com " + hash);

		cadena = "lsf1968";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("lsf1968 " + hash);

		cadena = "lsf1968@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("lsf1968@gmail.com " + hash);
		
		cadena = "munozm";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("munozm " + hash);

		cadena = "munozm@it.uc3m.es";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("munozm@it.uc3m.es " + hash);

		cadena = "corco36";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("corco36 " + hash);

		cadena = "corco36@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("corco36@gmail.com " + hash);

		cadena = "cristinacmp1988";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("cristinacmp1988 " + hash);

		cadena = "cristinacmp1988@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("cristinacmp1988@gmail.com " + hash);
		
		cadena = "jaf";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jaf@it.uc3m.es " + hash);

		cadena = "belen.uvigo";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("belen.uvigo@gmail.com " + hash);

		cadena = "pedro.arias.1965";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("pedro.arias.1965@gmail.com " + hash);

		
		cadena = "arcos1989";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("arcos1989 " + hash);

		cadena = "arcos1989@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("arcos1989@gmail.com " + hash);
		
		cadena = "luiss";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("luiss@it.uc3m.es " + hash);
		
		cadena = "jfisteus";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jfisteus@gmail.com " + hash);
		
		cadena = "ibernabe";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("ibernabe@gmail.com " + hash);
		
		cadena = "norberto.fdz";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("norberto.fdz@gmail.com " + hash);

		cadena = "jaor32";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jaor32@gmail.com " + hash);


		cadena = "prof.munozm";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("prof.munozm@gmail.com " + hash);

		cadena = "dfl_maradentro";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("dfl_maradentro@yahoo.es " + hash);

		cadena = "susanaladra";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("susanaladra@gmail.com " + hash);

		cadena = "lsoriamo";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("lsoriamo@gmail.com " + hash);

		cadena = "obseiker";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("obseiker@gmail.com " + hash);

		cadena = "alejandro.fdez";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("alejandro.fdez@gmail.com " + hash);

		cadena = "mariosoilan";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("mariosoilan@gmail.com " + hash);

		cadena = "artemisauc3m";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("artemisauc3m " + hash);

		cadena = "artemisauc3m@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("artemisauc3m@gmail.com " + hash);

		cadena = "artemisauc3m";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("artemisauc3m " + hash);

		cadena = "artemisauc3m@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("artemisauc3m@gmail.com " + hash);

		cadena = "lsf1968tv";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("lsf1968tv " + hash);

		cadena = "lsf1968tv@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("lsf1968tv@gmail.com " + hash);

		cadena = "jaalvarez";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jaalvarez " + hash);

		cadena = "jaalvarez@us.es";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("jaalvarez@us.es " + hash);		

		cadena = "juan.piz";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("juan.piz " + hash);		

		cadena = "juan.piz@gmail.com";
		hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
		System.out.println("juan.piz@gmail.com " + hash);		
	}
}