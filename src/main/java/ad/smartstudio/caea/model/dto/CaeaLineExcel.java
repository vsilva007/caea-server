package ad.smartstudio.caea.model.dto;

import com.poiji.annotation.ExcelCell;
import lombok.Data;

@Data
public class CaeaLineExcel {
	@ExcelCell(0)
	String type;
	@ExcelCell(3)
	String grup;
	@ExcelCell(4)
	String descripcio;
	@ExcelCell(5)
	String numero;
	@ExcelCell(6)
	String status;
}
