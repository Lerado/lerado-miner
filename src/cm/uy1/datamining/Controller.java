package cm.uy1.datamining;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import cm.uy1.datamining.dataminers.DataMinerHashtables;
import cm.uy1.datamining.dataminers.DataMinerLinkedList;
import cm.uy1.datamining.dataminers.DataMinerOptimized;
import cm.uy1.datamining.dataminers.DataMinerTable;
import cm.uy1.datamining.dataminers.datastructures.Item;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller implements Initializable{
	
	private AppServices myApp;
	
	@FXML
	private Stage myStage;
	
	@FXML
	private Scene myScene;
	
	@FXML
	private MenuButton algoMenu;
	
	@FXML
	private MenuItem algo1;
	
	@FXML
	private MenuItem algo2;
	
	@FXML
	private MenuItem algo3;
	
	@FXML
	private MenuItem algo4;
	
	private int algoNum = 0;
	
	@FXML
	private Button runAlgo;
	
	private boolean mustPrint = false;
	
	private File output;
	
	private File input;
	
	@FXML
	private TextArea log;
	
	@FXML
	private TextArea log2;
	
	@FXML
	private TextField outputPath;
	
	@FXML
	private TextField support;
	
	private double support_min = -1.0;
	
	@FXML
	private TextField confidence;
	
	private double confidence_min = -1.0;
	
	@FXML
	private Button popFileChooser;
	
	@FXML
	private Button listItems;
	
	@FXML
	private TextField itemInput;
	
	@FXML
	private AnchorPane miningPane;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab algorithms;
	
	@FXML
	private Tab client;
	
	@FXML
	private Tab statistic;
	
	private String item;
	
	@FXML
	private Button buyItem;
	
	@FXML
	private Button catalog;
	
	@FXML
	private Button frequentItems;
	
	@FXML
	private Button rarestItems;
	
	@FXML
	private Button stats;
	
	@FXML
	private Button minedRules;
	
	@FXML
	private TextArea prompt;
	
	@FXML
	private TextField itemProposal;
	
	@FXML
	private TextField outputPath1;
	
	@FXML
	private Label lab1;
	
	@FXML
	private Label lab2;
	
	@FXML
	private Label lab3;
	
	@FXML
	private Label lab4;
	
	@FXML
	private Label lab5;
	
	@FXML
	private Label lab6;
	
	@FXML
	private Label lab7;
	
	@FXML
	private Label lab8;
	
	@FXML
	private Label lab9;
	
	@FXML
	private Button popFileChooser1;
	
	@FXML
	private Button fetchData;
	
	@FXML
	private ProgressBar bar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		boolean check = AppServices.checkFiles();
		if(check)
			myApp = new AppServices();
		hide(!check);
		unhide(!check);
		
	}
	
	public void hide(boolean bool) {
		
		if(bool) {
			client.setDisable(true);
			statistic.setDisable(true);
			algorithms.setText("Setting dataset");
			algoMenu.setVisible(false);
			lab5.setVisible(false);
			lab6.setVisible(false);
			support.setVisible(false);
			confidence.setVisible(false);
			lab3.setVisible(false);
			lab4.setVisible(false);
			outputPath.setVisible(false);
			popFileChooser.setVisible(false);
			runAlgo.setVisible(false);
			lab7.setVisible(false);
			log.setVisible(false);
		}
	}
	
	public void revokeHide(boolean bool) {
		
		if(bool) {
			client.setDisable(false);
			statistic.setDisable(false);
			algorithms.setText("Mining algorithms");
			algoMenu.setVisible(true);
			lab5.setVisible(true);
			lab6.setVisible(true);
			support.setVisible(true);
			confidence.setVisible(true);
			lab3.setVisible(true);
			lab4.setVisible(true);
			outputPath.setVisible(true);
			popFileChooser.setVisible(true);
			runAlgo.setVisible(true);
			lab7.setVisible(true);
			log.setVisible(true);
		}
	}
	
	public void unhide(boolean bool) {
		
		if(bool) {
			lab1.setVisible(true);
			lab2.setVisible(true);
			outputPath1.setVisible(true);
			popFileChooser1.setVisible(true);
			fetchData.setVisible(true);
			
		}
	}
	
	public void revokeUnhide(boolean bool) {
		
		if(bool) {
			lab1.setVisible(false);
			lab2.setVisible(false);
			outputPath1.setVisible(false);
			popFileChooser1.setVisible(false);
			fetchData.setVisible(false);
			bar.setVisible(false);
			lab9.setVisible(false);
			lab8.setVisible(false);
			lab7.setVisible(false);
			
		}
	}
	
	public void loadMainPage(boolean bool) {
		
		myApp = new AppServices();
		revokeUnhide(bool);
		revokeHide(bool);
	}
	
	@FXML
	public void fetchData(final ActionEvent e) {
		
		if(input != null) {
			
			Task<Boolean> task = new Task<Boolean>() {
				boolean c = false;
				@Override
				protected Boolean call() {
					
					c = AppServices.save(input);
					if(c)
						Platform.runLater(()->{loadMainPage(true);});
					return null;
				}
				
			};
			
			Thread t = new Thread(task);
			t.setDaemon(true);
			t.start();
			loadLoader();
			
		}
		else
			errorPopup("Select a dataset file !");
	}
	
	public void loadLoader() {
		
		revokeUnhide(true);
		lab8.setVisible(true);
		bar.setVisible(true);
		lab9.setVisible(true);
	}
	
	@FXML
	public void changeMenuName1(final ActionEvent e) {
		algoMenu.setText(algo1.getText());
		algoNum = 1;
	}
	
	@FXML
	public void changeMenuName2(final ActionEvent e) {
		algoMenu.setText(algo2.getText());
		algoNum = 2;
	}
	
	@FXML
	public void changeMenuName3(final ActionEvent e) {
		algoMenu.setText(algo3.getText());
		algoNum = 3;
	}
	
	@FXML
	public void changeMenuName4(final ActionEvent e) {
		algoMenu.setText(algo4.getText());
		algoNum = 4;
	}
	
	public void log(String message) {
		
		log.setText(message);
	}
	
	public void logApp(String message) {
		
		log.appendText(message);
	}
	
	public void getSupport() {
		
		String input = support.getText();
		try {
			support_min = Double.valueOf(input).doubleValue();
		}
		catch(NumberFormatException e) {
			errorPopup("Bad support input.");
		}
	}
	
	public void getConfidence() {
		
		String input = confidence.getText();
		try {
			confidence_min = Double.valueOf(input).doubleValue();
		}
		catch(NumberFormatException e) {
			errorPopup("Bad confidence input.");
		}
	}
	
	public boolean checkInputs() {
		
		return support_min > 0 && support_min <= 100 && confidence_min > 0 && confidence_min <= 100;
	}
	
	@FXML
	public File popFileChooser(final ActionEvent e) {
		
		DirectoryChooser c = new DirectoryChooser();
		File f = c.showDialog(myStage);
		if(f != null)
			loadFilePath(f);
		else
			mustPrint = false;
		return f;
	}
	
	@FXML
	public void popFileChooser1(final ActionEvent e) {
		
		FileChooser c = new FileChooser();
		c.setTitle("Select dataset file");
		c.getExtensionFilters().add(new ExtensionFilter("Spreadsheet new format (*.xlsx)","*.xlsx"));
		File f = c.showOpenDialog(myStage);
		
		if(f != null) {
			if(checkExt(f)) {
				outputPath1.setText(f.getAbsolutePath());
				input = new File(f.getAbsolutePath());
			}
			else
				errorPopup("Choose a valid xlsx file !");
		}
	}
	
	public boolean checkExt(File f) {
		
		int lastIndexOf = f.getName().lastIndexOf(".");
		if(lastIndexOf == -1)
			return false;
		else
			return f.getName().substring(lastIndexOf).compareTo(".xlsx") == 0;
	}
	
	@FXML
	public void loadFilePath(File outputDir) {
		
		outputPath.setText(outputDir.getAbsolutePath()+File.separatorChar+"output.txt");
		mustPrint = true;
		output =  new File(outputDir,"output.txt");
	}
	
	@FXML
	public void runAlgorithm() {
		
		getSupport();
		getConfidence();
		if(checkInputs()) {
			
			switch(algoNum) {
			case 1:
				runAlgoTable();
				
				break;
				
			case 2:
				runAlgoLinkedList();
				
				break;
				
			case 3:
				runAlgoHashtables();
				
				break;
				
			case 4:
				runAlgoOptimized();
				
				break;
			default:
				log("Select an algorithm !");
			}
			
			if(mustPrint && algoNum != 0) {
				try {
					myApp.printResults(output,algoNum);
				} catch (ClassNotFoundException | IOException e) {
					
					errorPopup("");
				}
			}
		}
		else
			if(algoNum != 0)
				log("Invalid support or/and conficence entries.");
	}
	
	public void runAlgoTable() {
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				DataMinerTable dmt = new DataMinerTable();
				Platform.runLater(()->{log.clear();runAlgo.setDisable(true);logApp("Running Apriori (Table version) "
						+ "with a support of "
						+ support_min + "% " + "and a confidence of "+confidence_min+"%.\n\n");});
				dmt.configure(support_min, (double)confidence_min/100);
				dmt.aPriori();
				dmt.saveFrequentItemsets();
				dmt.saveRareItemsets();
				Platform.runLater(()->{logApp("Calculating association rules...");});
				dmt.calculateAssociationRules();
				dmt.saveAssociationRules();
				Platform.runLater(()->{
					runAlgo.setDisable(false);
					try {
						myApp.readFrequentItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readRareItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readAssociationRules();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.printResults(new File(new File(System.getProperty("user.home")
								+File.separatorChar
								+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar),"output.save")
								,algoNum);
						logApp(myApp.results());
						prompt.positionCaret(0);
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				return null;
			}
			
		};
		
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void runAlgoLinkedList() {
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				DataMinerLinkedList dml = new DataMinerLinkedList();
				Platform.runLater(()->{log.clear();runAlgo.setDisable(true);logApp("Running Apriori (Linked List version) "
						+ "with a support of "
						+ support_min + "% " + "and a confidence of "+confidence_min+"%.\n\n");});
				dml.configure(support_min, (double)confidence_min/100);
				dml.aPriori();
				dml.saveFrequentItemsets();
				dml.saveRareItemsets();
				Platform.runLater(()->{logApp("Calculating association rules...");});
				dml.calculateAssociationRules();
				dml.saveAssociationRules();
				Platform.runLater(()->{
					runAlgo.setDisable(false);
					try {
						myApp.readFrequentItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readRareItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readAssociationRules();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.printResults(new File(new File(System.getProperty("user.home")
								+File.separatorChar
								+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar),"output.save")
								,algoNum);
						logApp(myApp.results());
						prompt.positionCaret(0);
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				return null;
			}
			
		};
		
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void runAlgoHashtables() {
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				DataMinerHashtables dmh = new DataMinerHashtables();
				Platform.runLater(()->{log.clear();runAlgo.setDisable(true);logApp("Running Apriori (using hashtables) "
						+ "with a support of "
						+ support_min + "% " + "and a confidence of "+confidence_min+"%.\n\n");});
				dmh.configure(support_min, (double)confidence_min/100);
				dmh.aPriori();
				dmh.saveFrequentItemsets();
				dmh.saveRareItemsets();
				Platform.runLater(()->{logApp("Calculating association rules...");});
				dmh.calculateAssociationRules();
				dmh.saveAssociationRules();
				Platform.runLater(()->{
					runAlgo.setDisable(false);
					try {
						myApp.readFrequentItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readRareItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readAssociationRules();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.printResults(new File(new File(System.getProperty("user.home")
								+File.separatorChar
								+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar),"output.save")
								,algoNum);
						logApp(myApp.results());
						prompt.positionCaret(0);
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				return null;
			}
			
		};
		
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

	public void runAlgoOptimized() {
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				DataMinerOptimized dmo = new DataMinerOptimized();
				Platform.runLater(()->{log.clear();runAlgo.setDisable(true);logApp("Running Apriori (Optimized, Booleanised) "
						+ "with a support of "
						+ support_min + "% " + "and a confidence of "+confidence_min+"%.\n\n");});
				dmo.configure(support_min, (double)confidence_min/100);
				dmo.aPriori();
				dmo.saveFrequentItemsets();
				dmo.saveRareItemsets();
				Platform.runLater(()->{logApp("Calculating association rules...");});
				dmo.calculateAssociationRules();
				dmo.saveAssociationRules();
				Platform.runLater(()->{
					runAlgo.setDisable(false);
					try {
						myApp.readOptimizedFrequentItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readOptimizedRareItemsets();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.readOptimizedAssociationRules();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						myApp.printResults(new File(new File(System.getProperty("user.home")
								+File.separatorChar
								+"Data Mining App"+File.separatorChar+"archives"+File.separatorChar),"output.save")
								,algoNum);
						logApp(myApp.results());
						prompt.positionCaret(0);
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				return null;
			}
			
		};
		
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void listItems(final ActionEvent e) {
		
		log2.clear();
		log2.setText(myApp.listItems());
	}
	
	public void errorPopup(String message) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("We encoutered an issue.");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public void infoPopup(String message) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Successful operation.");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public void buy(final ActionEvent event) {
		
		item = itemInput.getText();
		
		if(!item.isBlank()) {
			if(myApp.searchInItems(item)) {
				
				infoPopup("Your purchase will be delivered to you soon.");
				
				try {
					int n = myApp.getLastAlgoNumber();
					switch(n) {
					case 3:
						try {
							myApp.readOptimizedAssociationRules();
							itemProposal.setText(myApp.nearerItem(new Item(item)));	
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
						break;
					default:
						try {
							myApp.readAssociationRules();
							itemProposal.setText(myApp.nearerItem(item));
						} catch (ClassNotFoundException | IOException e2) {
							e2.printStackTrace();
						}
					}
					
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					itemProposal.clear();
					errorPopup("You need to compute "
							+ "a data mining algorithm to enable item proposal.");
				}
			}

			else {
				itemProposal.clear();
				errorPopup("Item is not in store.");
			}
		}
		else {
			itemProposal.clear();
			errorPopup("Invalid input. Pick an item.");	
		}
	}
	
	@FXML
	public File popFileChooser() {
		
		DirectoryChooser c = new DirectoryChooser();
		File f = c.showDialog(myStage);
		return f;
	}
	
	@FXML
	public void catalog(final ActionEvent e) {
		
		File dir = popFileChooser();
		if(dir != null) {
			File f = new File(dir,"catalog.txt");
			printCatalog(f);
		}
	}
	
	public void printCatalog(File f) {
		
		try {
			int n = myApp.getLastAlgoNumber();
			myApp.printCatalog(f, n);
			infoPopup("Catalog successfully printed to "+ f.getAbsolutePath());
		} catch (ClassNotFoundException | IOException e) {
			
			try {
				myApp.printSimpleCatalog(f);
				infoPopup("Catalog successfully printed to "+ f.getAbsolutePath());
			} catch (IOException e1) {
				
				errorPopup("Contact administrator.");
			}
		}
	}
	
	public void prompt(String message) {
		
		prompt.appendText(message);
	}
	
	@SuppressWarnings("rawtypes")
	public void rarePrompt(ArrayList statistics) {
		
		String algoName = ((int)statistics.get(8) == 0)?"Apriori (Table version)":
			((int)statistics.get(8) == 1)?"Apriori (Linked lists version)":
				((int)statistics.get(8) == 2)?"Apriori (Hashtables usage)":
					((int)statistics.get(8) == 3)?"Apriori (Optimized, Booleanized)":"";
		prompt("Lastest run of "+algoName+" on the "+statistics.get(0)+".\n\n");
		prompt("Found a total of "+statistics.get(4)+" rare itemsets in "+statistics.get(1)+"ms.\n");
		prompt("Among them, rare groups of purchased items follow :\n\n");
		try {
			prompt(myApp.listRareItemsetsGroups((int)statistics.get(8)));
			prompt.positionCaret(0);
		} catch (ClassNotFoundException | IOException e) {
			
			errorPopup("");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void frequentPrompt(ArrayList statistics) {
		
		String algoName = ((int)statistics.get(8) == 0)?"Apriori (Table version)":
			((int)statistics.get(8) == 1)?"Apriori (Linked lists version)":
				((int)statistics.get(8) == 2)?"Apriori (Hashtables usage)":
					((int)statistics.get(8) == 3)?"Apriori (Optimized, Booleanized)":"";
		prompt("Lastest run of "+algoName+" on the "+statistics.get(0)+".\n\n");
		prompt("Found a total of "+statistics.get(3)+" frequent itemsets in "+statistics.get(1)+"ms.\n");
		prompt("Among them, the most frequent groups of purchased items follow :\n\n");
		try {
			prompt(myApp.listFrequentItemsetsGroups((int)statistics.get(8)));
			prompt.positionCaret(0);
		} catch (ClassNotFoundException | IOException e) {
			
			errorPopup("");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void rulesPrompt(ArrayList statistics) {
		
		String algoName = ((int)statistics.get(8) == 0)?"Apriori (Table version)":
			((int)statistics.get(8) == 1)?"Apriori (Linked lists version)":
				((int)statistics.get(8) == 2)?"Apriori (Hashtables usage)":
					((int)statistics.get(8) == 3)?"Apriori (Optimized, Booleanized)":"";
		prompt("Lastest run of "+algoName+" on the "+statistics.get(0)+".\n\n");
		prompt("Found a total of "+statistics.get(5)+" association rules in "+statistics.get(2)+"ms.\n");
		prompt("They are :\n\n");
		try {
			prompt(myApp.listAssociationRules((int)statistics.get(8)));
			prompt.positionCaret(0);
		} catch (ClassNotFoundException | IOException e) {
			
			errorPopup("");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void statsPrompt(ArrayList statistics) {
		
		String algoName = ((int)statistics.get(8) == 0)?"Apriori (Table version)":
			((int)statistics.get(8) == 1)?"Apriori (Linked lists version)":
				((int)statistics.get(8) == 2)?"Apriori (Hashtables usage)":
					((int)statistics.get(8) == 3)?"Apriori (Optimized, Booleanized)":"";
		prompt("Lastest run of "+algoName+" on the "+statistics.get(0)+".\n\n");
		prompt("Parameters: -- Support   : "+statistics.get(6)+".\n");
		prompt("                    -- Confidence: "+(double)statistics.get(7)*100+"%.\n\n");
		prompt("Results: \n\n");
		prompt("-- Found a total of "+statistics.get(3)+" frequent itemsets in "+statistics.get(1)+"ms.\n");
		prompt("-- Found a total of "+statistics.get(4)+" rare itemsets within "+statistics.get(1)+"ms.\n");
		prompt("-- Found a total of "+statistics.get(5)+" association rules in "+statistics.get(2)+"ms.\n");
		prompt("\n Algorithm Execution duration: "+((long)statistics.get(1)+(long)statistics.get(2))+".");
	}
	
	public void rarestItems(final ActionEvent e) {
		
		prompt.clear();
		try {
			myApp.readStatistics();
			rarePrompt(myApp.statistics());
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			prompt.clear();
			errorPopup("You need to compute "
					+ "a data mining algorithm to enable this function.");
		}
	}
	
	public void frequentItems(final ActionEvent e) {
		
		prompt.clear();
		try {
			myApp.readStatistics();
			frequentPrompt(myApp.statistics());
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			prompt.clear();
			errorPopup("You need to compute "
					+ "a data mining algorithm to enable this function.");
		}
	}
	
	public void minedRules(final ActionEvent e) {
		
		prompt.clear();
		try {
			myApp.readStatistics();
			rulesPrompt(myApp.statistics());
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			prompt.clear();
			errorPopup("You need to compute "
					+ "a data mining algorithm to enable this function.");
		}
	}
	
	public void statistics(final ActionEvent e) {
		
		prompt.clear();
		try {
			myApp.readStatistics();
			statsPrompt(myApp.statistics());
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			prompt.clear();
			errorPopup("You need to compute "
					+ "a data mining algorithm to enable this function.");
		}
	}

}
