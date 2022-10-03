package ad.smartstudio.caea.model.dto;

import ad.smartstudio.caea.model.entity.Activitat;
import ad.smartstudio.caea.model.entity.Grup;
import lombok.Data;

@Data
public class CaeaTableLine {
	String grup;
	String descripcio;
	String nombre;
	String estat;
	Boolean status = Boolean.TRUE;
	Boolean bold = Boolean.FALSE;

	public CaeaTableLine(Activitat activitat) {
		this.grup = activitat.getClassification();
		this.descripcio = activitat.getDescription();
		this.nombre = String.valueOf(activitat.getQuantity());
		this.status = activitat.getStatus();
		this.estat = this.status ? "Obert" : "Tancat";
	}
	//	public CaeaTableLine(Seccio seccio) {  // TODO not needed for now
	//		this.grup = seccio.getClassification();
	//		this.descripcio = seccio.getDescription();
	//		this.nombre = String.valueOf(seccio.getQuantity());
	//		this.estat = seccio.getStatus() ? "Obert" : "Tancat";
	//	}

	public CaeaTableLine(Grup grup) {
		this.grup = grup.getClassification();
		this.descripcio = grup.getDescription();
		this.nombre = String.valueOf(grup.getQuantity());
		grup.getActivitats().forEach(act -> this.status = this.status && act.getStatus());
		this.estat = this.status ? "Obert" : "Tancat";
	}

	@Override
	//	@formatter:off
	public String toString() {
		return "<tr style=\"border-collapse:collapse;\">"
				+ createCell(grup)
				+ createCell(descripcio)
				+ createCell(nombre)
				+ createCell(estat)
				+ "</tr>";
	}

	private String createCell(String property){
		if (bold)
			return "<td align=\"left\" style=\"padding:0;Margin:0;font-weight: bold;\">" + property + "</td>";
		return "<td align=\"left\" style=\"padding:0;Margin:0;\">" + property + "</td>";
	}
}
