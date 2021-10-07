package com.vone.medisafe.antrian;

import java.io.Serializable;

public class MsAntrian implements Serializable{
	
	private Integer idAntrian;
	private Integer delayAntrian;
	private Integer delayApotik;
	private String  antrianDokter;
	private String antrianApotik;
	
	public Integer getIdAntrian() {
		return idAntrian;
	}
	public void setIdAntrian(Integer idAntrian) {
		this.idAntrian = idAntrian;
	}
	public Integer getDelayAntrian() {
		return delayAntrian;
	}
	public void setDelayAntrian(Integer delayAntrian) {
		this.delayAntrian = delayAntrian;
	}
	public Integer getDelayApotik() {
		return delayApotik;
	}
	public void setDelayApotik(Integer delayApotik) {
		this.delayApotik = delayApotik;
	}
	public String getAntrianDokter() {
		return antrianDokter;
	}
	public void setAntrianDokter(String antrianDokter) {
		this.antrianDokter = antrianDokter;
	}
	public String getAntrianApotik() {
		return antrianApotik;
	}
	public void setAntrianApotik(String antrianApotik) {
		this.antrianApotik = antrianApotik;
	}

}
