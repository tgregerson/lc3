package lc3sim.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lc3sim.ui.UIState.MemoryEntry;

public class UIFXMain extends Application {
  
  public UIFXMain() {
    InitMemory();
    controller_ = new lc3sim.core.SimulationController();
    lc3sim.core.ArchitecturalState model = new lc3sim.core.ArchitecturalState();
    controller_.SetModel(model);
    controller_.SetView(this);
    controller_.TestLoadP1();
  }
  
  private void InitMemory() {
    memory_table_ = new TableView<MemoryEntry>(); 
    memory_table_.setEditable(true);

    memory_contents_ = new HashMap<Integer, MemoryEntry>();
    ObservableList<MemoryEntry> observable_list =
        FXCollections.observableArrayList();
    for (int i = 0; i < kAddressLimit; ++i) {
      MemoryEntry entry = new MemoryEntry(i, 0);
      memory_contents_.put(i, entry);
      observable_list.add(entry);
    }

	  TableColumn<MemoryEntry, String> address_col = new TableColumn<MemoryEntry, String>("Address");
	  TableColumn<MemoryEntry, String> data_col = new TableColumn<MemoryEntry, String>("Data");
	  TableColumn<MemoryEntry, String> inst_col = new TableColumn<MemoryEntry, String>("Instruction");
    memory_table_.getColumns().add(address_col);
    memory_table_.getColumns().add(data_col);
    memory_table_.getColumns().add(inst_col);
    address_col.setMinWidth(100);
    data_col.setMinWidth(100);
    inst_col.setMinWidth(300);
    
    address_col.setCellValueFactory(
        new PropertyValueFactory<MemoryEntry, String>("addressString"));
    data_col.setCellValueFactory(
        new PropertyValueFactory<MemoryEntry, String>("dataString"));
    inst_col.setCellValueFactory(
        new PropertyValueFactory<MemoryEntry, String>("instructionString"));

    data_col.setCellFactory(TextFieldTableCell.<MemoryEntry>forTableColumn());
    data_col.setOnEditCommit(
      new EventHandler<CellEditEvent<MemoryEntry, String>>() {
        @Override
        public void handle(CellEditEvent<MemoryEntry, String> event) {
          ((MemoryEntry)event.getTableView().getItems().get(
              event.getTablePosition().getRow())
              ).setDataString(event.getNewValue());
          ForceUpdate(event.getTableView());
        }
      }
    );
    
    memory_table_.setItems(observable_list);
  }
  
  public void UpdateMemory(int address, int data) {
    MemoryEntry e = memory_contents_.get(address);
    e.setData(data);
    ForceUpdate(memory_table_);
  }
  
	@Override
	public void start(Stage stage) {
	  Scene scene = new Scene(new Group());
	  stage.setTitle("Table View");
	  stage.setWidth(200);
	  stage.setHeight(500);
	  
	  final Label label = new Label("Memory Contents");
	  label.setFont(new Font("Arial", 20));
	  
	  final VBox vbox = new VBox();
	  vbox.setSpacing(5);
	  vbox.setPadding(new Insets(10, 0, 0, 10));
	  vbox.getChildren().addAll(label, memory_table_);
	  
	  ((Group)scene.getRoot()).getChildren().addAll(vbox);
	  
	  stage.setScene(scene);
	  stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

  private void ForceUpdate(TableView<?> v) {
    if (v.getColumns().size() > 0) {
      v.getColumns().get(0).setVisible(false);
      v.getColumns().get(0).setVisible(true);
    }
  }
  
  private lc3sim.core.SimulationController controller_;

	// Maps from address (as unsigned int) to MemoryEntry.
	private Map<Integer, MemoryEntry> memory_contents_;
	private TableView<MemoryEntry> memory_table_;
	
	private final int kAddressBits = lc3sim.core.Memory.kNumAddrBits;
	private final int kAddressLimit = 2 << (kAddressBits - 1);
}
