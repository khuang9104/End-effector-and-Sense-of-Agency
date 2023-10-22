package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteToExcel {

	private String fileAddr = null;
	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private DataSaveAddressChecker dataAddressChecker = new DataSaveAddressChecker();

	public void write(String name, int[] blockOrder, StoredDoubles[] data_estimationError,
			StoredDoubles[] data_actualTime, StoredDoubles[] data_perceivedTime, StoredDoubles[] data_startTimeMicro,
			StoredDoubles[] data_actionTimeMicro, StoredDoubles[] data_beepTriggerTimeMicro, StoredDoubles[] data_actualBeepTimeMicro, StoredDoubles[] data_actionTimeLocal)
			throws IOException, WriteException {

		dataAddressChecker.checkAddressExist();
		this.fileAddr = dataAddressChecker.getDataSaveAddress();
		File file = new File(fileAddr + name);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

		setFormat();
		workbook.createSheet("Trail Info", 0);
		addCaption(workbook.getSheet(0), 0, 0, "Trail Order");

		for (int i = 0; i < blockOrder.length; i++) {
			workbook.createSheet("Type " + String.valueOf(blockOrder[i] + 1) + " Block", i + 1);
			addCaption(workbook.getSheet(0), 0, i + 1, String.valueOf(blockOrder[i] + 1));
		}

		for (int i = 0; i < data_estimationError.length; i++) {
			WritableSheet excelSheet = workbook.getSheet(i + 1);
			createLabels(excelSheet);
			createContentNumber(excelSheet, data_estimationError[i].doubles, 1, 0);
			createContentNumber(excelSheet, data_actualTime[i].doubles, 1, 1);
			createContentNumber(excelSheet, data_perceivedTime[i].doubles, 1, 2);

			createContentNumber(excelSheet, data_startTimeMicro[i].doubles, 1, 4);
			createContentNumber(excelSheet, data_actionTimeMicro[i].doubles, 1, 5);
			createContentNumber(excelSheet, data_beepTriggerTimeMicro[i].doubles, 1, 6);
			createContentNumber(excelSheet, data_actualBeepTimeMicro[i].doubles, 1, 7);
			
			createContentNumber(excelSheet, data_actionTimeLocal[i].doubles, 1, 9);  // ssSSS Second+Millisecond
		}
		workbook.write();
		workbook.close();
	}

	private void createLabels(WritableSheet sheet) throws WriteException {
		addCaption(sheet, 0, 0, "Estimation Error(ms)");
		addCaption(sheet, 1, 0, "Actual Time(ms)");
		addCaption(sheet, 2, 0, "Peceived Time(ms)");

		addCaption(sheet, 4, 0, "Start Time(μs)");
		addCaption(sheet, 5, 0, "Action Time(μs)");
		addCaption(sheet, 6, 0, "Beep Trigger Time(μs)");
		addCaption(sheet, 7, 0, "Actual Beep Time(μs)");
		
		addCaption(sheet, 9, 0, "Action Time (ssSSS)");
	}

	private void createContentNumber(WritableSheet sheet, double[] nums, int startRow, int column)
			throws WriteException, RowsExceededException {
		// Write a few number
		for (int i = 0; i < nums.length; i++) {
			addDouble(sheet, column, i + startRow, nums[i]);
		}
	}
	
	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	private void addDouble(WritableSheet sheet, int column, int row, Double db)
			throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, db, times);
		sheet.addCell(number);
	}

	private void setFormat() throws WriteException {
		WritableFont times12pt = new WritableFont(WritableFont.TIMES, 12);
		times = new WritableCellFormat(times12pt);
		times.setWrap(true);

		WritableFont times12ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times12ptBoldUnderline);
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);
	}
}