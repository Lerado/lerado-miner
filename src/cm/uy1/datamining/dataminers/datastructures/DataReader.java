package cm.uy1.datamining.dataminers.datastructures;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cm.uy1.newObjects.BinaryTree;

@SuppressWarnings("unused")
public class DataReader {
	
	protected File datasetExcelFile;
	protected XSSFWorkbook currentExcelWorkbook;
	protected XSSFSheet currentSheet;
	protected Iterator<Row> rowIterator;
	protected Iterator<Cell> cellIterator;
	protected ArrayList<String> aRow;
	protected ArrayList<String> aTransaction;
	protected ArrayList<ArrayList<String>> stack;
	protected long currentRowNumber;
	
	public DataReader() {
		
	}
	
	public DataReader(File datasetFile) {
		
		try {
			datasetExcelFile = datasetFile;
			currentExcelWorkbook = new XSSFWorkbook(datasetExcelFile);
			currentSheet = currentExcelWorkbook.getSheetAt(0);
			rowIterator = currentSheet.rowIterator();
			rowIterator.next();
			currentRowNumber = 1;
			stack = new ArrayList<ArrayList<String>>();
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public ArrayList<String> getTransaction() {
		
		return this.aTransaction;
	}
	
	public long getCurrentRowNumber() {
		
		return this.currentRowNumber;
	}
	
	public String getFilePath() {
		
		return this.datasetExcelFile.getAbsolutePath();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void nextRow() {
		
		aRow = new ArrayList();
		
		if(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			currentRowNumber++;
			cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch(cell.getCellType()) {
				case STRING:
					aRow.add(cell.getStringCellValue());
					break;
				case NUMERIC:
					aRow.add(String.valueOf(cell.getNumericCellValue()));
					break;
				default:
					
				}	
			}
		}
	}
	
	public void nextTransaction() {
		
		aTransaction = new ArrayList<String>();
		nextRow();
		String invoiceNum= aRow.get(0);
		
		if(!stack.isEmpty()) {
			invoiceNum= stack.get(0).get(0);
			aTransaction.add(stack.remove(0).get(1));
		}
		
		if(invoiceNum.compareTo(aRow.get(0)) == 0) {
			do {
				aTransaction.add((String) aRow.get(1));
				invoiceNum = aRow.get(0);
				nextRow();
				if(aRow.isEmpty())
					break;
			}
			while(invoiceNum.compareTo(aRow.get(0)) == 0);
		}
		
		stack.add(aRow); 
	}
	
	public boolean canStillRead() {
		
		return !this.aRow.isEmpty();
	}
	
	public void close() throws IOException {
		
		currentExcelWorkbook.close();
	}
	
	public void flush() {
		
		currentSheet = currentExcelWorkbook.getSheetAt(0);
		rowIterator = currentSheet.rowIterator();
		rowIterator.next();
		cellIterator = null;
		currentRowNumber = 1;
		stack = new ArrayList<ArrayList<String>>();
		aTransaction = null;
		aRow = null;
	}
	
	public void saveItems() {
		
		this.flush();
		BinaryTree<String> tree = new BinaryTree<String>();
		tree.useAsBinarySearchTree();
		this.nextRow();
		
		while(this.canStillRead()) {
			tree.insert(aRow.get(1));
			this.nextRow();
		}
		
		ObjectOutputStream oos;
		try {
			File dir = new File(System.getProperty("user.home")
					+File.separatorChar
					+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar);
			if(!dir.exists())
				dir.mkdirs();
			
			oos = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(
									new File(dir,"items.save"))));
			oos.writeObject(tree.toArrayList());
			oos.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void saveTransactions() {
		
		this.flush();
		ArrayList<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
		this.nextTransaction();
		
		while(this.canStillRead()) {
			transactions.add(aTransaction);
			this.nextTransaction();
		}
		transactions.add(aTransaction);
		
		ObjectOutputStream oos;
		try {
			File dir = new File(System.getProperty("user.home")
					+File.separatorChar
					+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar);
			if(!dir.exists())
				dir.mkdirs();
			
			oos = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(
									new File(dir,"transactions.save"))));
			oos.writeObject(transactions);
			oos.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
}
