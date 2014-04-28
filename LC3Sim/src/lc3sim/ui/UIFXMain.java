package lc3sim.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lc3sim.ui.UIState.*;

public class UIFXMain extends Application {
  
  public UIFXMain() {
    InitButtons();
    InitMemory();
    InitGpr();
    InitSpr();
    controller_ = new lc3sim.core.SimulationController();
    lc3sim.core.ArchitecturalState model = new lc3sim.core.ArchitecturalState();
    controller_.SetModel(model);
    controller_.SetView(this);
    controller_.TestLoadP1();
  }
  
  private void InitButtons() {
    step_into_button_ = new Button("Step Into");
    step_into_button_.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        controller_.StepInto();
      }
    });
  }

  private void InitSpr() {
    spr_table_ = new TableView<RegisterEntry>(); 
    spr_table_.setEditable(true);

    spr_contents_ = new HashMap<String, RegisterEntry>();
    ObservableList<RegisterEntry> observable_list =
        FXCollections.observableArrayList();

    String[] spr_names = {"PC", "IR"};
    for (String name : spr_names) {
      RegisterEntry entry = new RegisterEntry(name, 0);
      spr_contents_.put(name, entry);
      observable_list.add(entry);
    }

	  TableColumn<RegisterEntry, String> name_col = new TableColumn<RegisterEntry, String>("Name");
	  TableColumn<RegisterEntry, String> data_col = new TableColumn<RegisterEntry, String>("Data");
    spr_table_.getColumns().add(name_col);
    spr_table_.getColumns().add(data_col);
    spr_table_.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
    name_col.setCellValueFactory(
        new PropertyValueFactory<RegisterEntry, String>("nameString"));
    data_col.setCellValueFactory(
        new PropertyValueFactory<RegisterEntry, String>("dataString"));

    data_col.setCellFactory(TextFieldTableCell.<RegisterEntry>forTableColumn());
    data_col.setOnEditCommit(
      new EventHandler<CellEditEvent<RegisterEntry, String>>() {
        @Override
        public void handle(CellEditEvent<RegisterEntry, String> event) {
          RegisterEntry entry = 
              ((RegisterEntry)event.getTableView().getItems().get(
                  event.getTablePosition().getRow()));
          entry.setDataString(event.getNewValue());
          switch(entry.getNameString()) {
            case "PC":
              controller_.SetModelPc(entry.getData());
              break;
            default:
              throw new RuntimeException(
                  "Unrecognized SPR: " + entry.getNameString());
          }
          ForceUpdate(event.getTableView());
        }
      }
    );
    
    spr_table_.setItems(observable_list);
  }

  private void InitGpr() {
    gpr_table_ = new TableView<RegisterEntry>(); 
    gpr_table_.setEditable(true);

    gpr_contents_ = new HashMap<String, RegisterEntry>();
    ObservableList<RegisterEntry> observable_list =
        FXCollections.observableArrayList();
    for (int i = 0; i < kGprAddressLimit; ++i) {
      String name = "R" + i;
      RegisterEntry entry = new RegisterEntry(name, 0);
      gpr_contents_.put(name, entry);
      observable_list.add(entry);
    }

	  TableColumn<RegisterEntry, String> name_col = new TableColumn<RegisterEntry, String>("Name");
	  TableColumn<RegisterEntry, String> data_col = new TableColumn<RegisterEntry, String>("Data");
    gpr_table_.getColumns().add(name_col);
    gpr_table_.getColumns().add(data_col);
    gpr_table_.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
    name_col.setCellValueFactory(
        new PropertyValueFactory<RegisterEntry, String>("nameString"));
    data_col.setCellValueFactory(
        new PropertyValueFactory<RegisterEntry, String>("dataString"));

    data_col.setCellFactory(TextFieldTableCell.<RegisterEntry>forTableColumn());
    data_col.setOnEditCommit(
      new EventHandler<CellEditEvent<RegisterEntry, String>>() {
        @Override
        public void handle(CellEditEvent<RegisterEntry, String> event) {
          RegisterEntry entry = 
              ((RegisterEntry)event.getTableView().getItems().get(
                  event.getTablePosition().getRow()));
          entry.setDataString(event.getNewValue());
          int reg_num = Integer.parseInt(entry.getNameString().substring(1));
          controller_.SetModelGpr(reg_num, entry.getData());
          ForceUpdate(event.getTableView());
        }
      }
    );
    
    gpr_table_.setItems(observable_list);
  }
  
  private void InitMemory() {
    memory_table_ = new TableView<MemoryEntry>(); 
    memory_table_.setEditable(true);

    memory_contents_ = new HashMap<Integer, MemoryEntry>();
    ObservableList<MemoryEntry> observable_list =
        FXCollections.observableArrayList();
    for (int i = 0; i < kMemoryAddressLimit; ++i) {
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
    address_col.setMinWidth(80);
    data_col.setMinWidth(80);
    inst_col.setMinWidth(150);
    memory_table_.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
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
          MemoryEntry entry = 
              ((MemoryEntry)event.getTableView().getItems().get(
                  event.getTablePosition().getRow()));
          entry.setDataString(event.getNewValue());
          controller_.SetModelMemory(entry.getAddress(), entry.getData());
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

  public void UpdateGpr(int address, int data) {
    String name = "R" + address;
    RegisterEntry e = gpr_contents_.get(name);
    e.setData(data);
    ForceUpdate(gpr_table_);
  }

  public void UpdateSpr(String name, int data) {
    RegisterEntry e = spr_contents_.get(name);
    e.setData(data);
    ForceUpdate(spr_table_);
  }
  
	@Override
	public void start(Stage stage) {
	  Scene scene = new Scene(new Group());
	  stage.setTitle("Table View");
	  stage.setWidth(800);
	  stage.setHeight(700);
	  
	  final VBox v_layout = new VBox(5);
	  
	  final HBox button_box = new HBox();
	  button_box.setSpacing(5);
	  button_box.setPadding(new Insets(10, 0, 0, 10));
	  button_box.getChildren().addAll(step_into_button_);
	  
	  final HBox state_box = new HBox();
	  state_box.setSpacing(5);
	  state_box.setPadding(new Insets(10, 0, 0, 10));

	  final VBox spr_box = new VBox(5);
	  final Label spr_label = new Label("SPR");
	  spr_label.setFont(new Font("Arial", 20));
	  spr_box.getChildren().addAll(spr_label, spr_table_);
	  spr_box.setMaxWidth(150);
	  state_box.getChildren().addAll(spr_box);

	  final VBox gpr_box = new VBox(5);
	  final Label gpr_label = new Label("GPR");
	  gpr_label.setFont(new Font("Arial", 20));
	  gpr_box.getChildren().addAll(gpr_label, gpr_table_);
	  gpr_box.setMaxWidth(150);
	  state_box.getChildren().addAll(gpr_box);

	  final VBox mem_box = new VBox(5);
	  final Label mem_label = new Label("Memory Contents");
	  mem_label.setFont(new Font("Arial", 20));
	  mem_box.getChildren().addAll(mem_label, memory_table_);
	  mem_box.setMinWidth(350);
	  state_box.getChildren().addAll(mem_box);

	  v_layout.getChildren().addAll(button_box, state_box);

	  ((Group)scene.getRoot()).getChildren().addAll(v_layout);
	  
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
  
  private Button step_into_button_;

	// Maps from address (as unsigned int) to MemoryEntry.
	private Map<Integer, MemoryEntry> memory_contents_;
	private TableView<MemoryEntry> memory_table_;

	// Maps from register name (as String) to RegisterEntry.
	private Map<String, RegisterEntry> gpr_contents_;
	private TableView<RegisterEntry> gpr_table_;

	// Maps from register name (as String) to RegisterEntry.
	private Map<String, RegisterEntry> spr_contents_;
	private TableView<RegisterEntry> spr_table_;
	
	private final int kMemoryAddressBits = lc3sim.core.Memory.kNumAddrBits;
	private final int kMemoryAddressLimit = 2 << (kMemoryAddressBits - 1);
	private final int kGprAddressBits = lc3sim.core.RegisterFile.kNumAddrBits;
	private final int kGprAddressLimit = 2 << (kGprAddressBits - 1);
}
