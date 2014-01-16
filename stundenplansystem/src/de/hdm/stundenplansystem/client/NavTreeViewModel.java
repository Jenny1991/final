/**
 * 
 */
package de.hdm.stundenplansystem.client;

//import java.util.HashMap;

import java.util.List;
//import java.util.Map;
import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
//import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;



import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.BusinessObjekt;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Zeitslot;


/**
 * @author C. Rathke, V. Hofmann, J. Espich
 *
 */
public class NavTreeViewModel extends Content implements TreeViewModel {

	
	private DozentForm df;
	private LehrveranstaltungForm lf;
	private RaumForm rf;
	private SemesterverbandForm svf;
	private StudiengangForm sgf;

	private CreateDozent cd;
	private CreateLehrveranstaltung cl;
	private CreateRaum cr;
	private CreateSemesterverband csv;
	private CreateStudiengang csg;
	
	
	private Dozent selectedDozent = null;
	private Lehrveranstaltung selectedLv = null;
	private Raum selectedRaum = null;
	private Zeitslot selectedZs = null;
	private Semesterverband selectedSv = null;
	private Studiengang selectedSg = null;
	
	private VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	private ListDataProvider<Dozent> dozentDataProvider;
	private ListDataProvider<Lehrveranstaltung> lvDataProvider;
	private ListDataProvider<Raum> raumDataProvider;
	private ListDataProvider<Zeitslot> zsDataProvider;
	private ListDataProvider<Semesterverband> svDataProvider;
	private ListDataProvider<Studiengang> sgDataProvider;
	private ListDataProvider<String> stringDataProvider;
	
	private Stundenplansystem sps;
	//private Map<Dozent, ListDataProvider<Lehrveranstaltung>> lvDataProvider = new HashMap<Dozent, ListDataProvider<Lehrveranstaltung>>();

	private ProvidesKey<Object> boKeyProvider = new ProvidesKey<Object>() {
		public Integer getKey(Object object) {
		
			
			if (object == null) {
				return null;
			}
			
			else if (object instanceof String) {
				return new Integer(((String)object).hashCode());
			}			
			else if (object instanceof Dozent) {
				return new Integer(((Dozent)object).getId());
			} else {
				return new Integer(-((Dozent)object).getId());
			}
/**			else if (object instanceof Lehrveranstaltung) {
				return new Integer(((Lehrveranstaltung)object).getId());
			}
			else {
				return new Integer(-((Lehrveranstaltung)object).getId());
			}
			else if (object instanceof Raum) {
				return new Integer(((Raum)object).getId());
			}
			else {
				return new Integer(-((Raum)object).getId());
			}
			else if (object instanceof Semesterverband) {
				return new Integer(((Semesterverband)object).getId());
			}
			else {
				return new Integer(-((Semesterverband)object).getId());
			}
			else if (object instanceof Studiengang) {
				return new Integer(((Studiengang)object).getId());
			}
			else {
				return new Integer(-((Studiengang)object).getId());
			} */
		} 
	};
	
	private SingleSelectionModel <Object> selectionModel = new SingleSelectionModel<Object>(boKeyProvider);
	
	public NavTreeViewModel(CreateDozent cd, CreateLehrveranstaltung cl, CreateRaum cr, CreateStudiengang csg, CreateSemesterverband csv, DozentForm df, LehrveranstaltungForm lf, RaumForm rf, StudiengangForm sgf, SemesterverbandForm svf, Stundenplansystem sps) {
		
	//public NavTreeViewModel(CreateDozent cd, CreateLehrveranstaltung cl, CreateRaum cr, CreateStudiengang csg, CreateSemesterverband csv, Stundenplansystem sps) {
		this.cd = cd;
		cd.setTvm(this);
		this.cl = cl;
		cl.setTvm(this);
		this.cr = cr;
		cr.setTvm(this);
		this.csg = csg;
		csg.setTvm(this);
		this.csv = csv;
		csv.setTvm(this);
		
		this.df = df;
		df.setTvm(this);
		this.lf = lf;
		lf.setTvm(this);
		this.rf = rf;
		rf.setTvm(this);
		this.sgf = sgf;
		sgf.setTvm(this);
		this.svf = svf;
		svf.setTvm(this);
	
	
		this.sps = sps;
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Object selection = selectionModel.getSelectedObject();
				
				if (selection instanceof String && (String)selection == "Dozent anlegen") {
					setCreateDozent();
				}
				
				if (selection instanceof String && (String)selection == "Dozent verwalten") {
					showDozentForm();
				}
				
				if (selection instanceof String && (String)selection == "Lehrveranstaltung anlegen") {
					setCreateLehrveranstaltung();
				}
				
				if (selection instanceof String && (String)selection == "Lehrveranstaltung verwalten") {
					showLehrveranstaltungForm();
				}
				
				if (selection instanceof String && (String)selection == "Raum anlegen") {
					setCreateRaum();
				}
				
				if (selection instanceof String && (String)selection == "Raum verwalten") {
					showRaumForm();
				}
				
				if (selection instanceof String && (String)selection == "Studiengang anlegen") {
					setCreateStudiengang();
				}
				
				if (selection instanceof String && (String)selection == "Studiengang verwalten") {
					showStudiengangForm();
				}
				
				if (selection instanceof String && (String)selection == "Semesterverband anlegen") {
					setCreateSemesterverband();
				}
				
				if (selection instanceof String && (String)selection == "Semesterverband verwalten") {
					showSemesterverbandForm();
				}
				
				if (selection instanceof Dozent) {
					setSelectedDozent((Dozent) selection);
				} 
				
				if (selection instanceof Lehrveranstaltung) {
					setSelectedLv((Lehrveranstaltung) selection);
				}
				
				if (selection instanceof Raum) {
					setSelectedRaum((Raum) selection);
				}
				
				if (selection instanceof Studiengang) {
					setSelectedSg((Studiengang) selection);
				}
				
				if (selection instanceof Semesterverband) {
					setSelectedSv((Semesterverband) selection);
				}
			}

		});
	}
	

	Dozent getSelectedDozent() {
		return selectedDozent;
	}
	
	void setCreateDozent() {
		sps.createDozentForm();
	}
	
	void setSelectedDozent(Dozent d) {
		selectedDozent = d;
		df.setSelected(d);
	}
	
	void showDozentForm() {
		sps.showDozentForm();
	}
	
	Lehrveranstaltung getSelectedLv() {
		return selectedLv;
	}
	
	void setCreateLehrveranstaltung(){
		sps.createLvForm();
	}
	
	void setSelectedLv(Lehrveranstaltung lv) {
		selectedLv = lv;
		lf.setSelected(lv);
	}
	
	void showLehrveranstaltungForm() {
		sps.showLehrveranstaltungForm();
	}
	
	Raum getSelectedRaum() {
		return selectedRaum;
	}
	void setCreateRaum(){
		sps.createRaumForm();
	}
	
	void setSelectedRaum(Raum r) {
		selectedRaum = r;
		rf.setSelected(r);
	}
	
	void showRaumForm() {
		sps.showRaumForm();
	}
	
	Studiengang getSelectedSg() {
		return selectedSg;
	}
	
	void setCreateStudiengang(){
		sps.createSgForm();
	}
	
	void setSelectedSg(Studiengang sg) {
		selectedSg = sg;
		sgf.setSelected(sg);
	}
	
	void showStudiengangForm() {
		sps.showStudiengangForm();
	}
	
	Semesterverband getSelectedSv() {
		return selectedSv;
	}
	
	void setCreateSemesterverband(){
		sps.createSvForm();
	}
	
	void setSelectedSv(Semesterverband sv) {
		selectedSv = sv;
		svf.setSelected(sv);
	}
	
	void showSemesterverbandForm() {
		sps.showSemesterverbandForm();
	}
	
	void addDozent(Dozent dozent) {
		dozentDataProvider.getList().add(dozent);
	}

	void addRaum(Raum raum) {
		raumDataProvider.getList().add(raum);
		//lvDataProvider.put(dozent, new ListDataProvider<Lehrveranstaltung>());
	}
	
	void addStudiengang(Studiengang studiengang) {
		sgDataProvider.getList().add(studiengang);
		//lvDataProvider.put(dozent, new ListDataProvider<Lehrveranstaltung>());
	}
	
	void addSemesterverband(Semesterverband semesterverband) {
		svDataProvider.getList().add(semesterverband);
		//lvDataProvider.put(dozent, new ListDataProvider<Lehrveranstaltung>());
	}
	
	void addLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		lvDataProvider.getList().add(lehrveranstaltung);
		//lvDataProvider.put(dozent, new ListDataProvider<Lehrveranstaltung>());
	}
	
	void updateDozent(Dozent dozent) {
		List<Dozent> dozentList = dozentDataProvider.getList();
		int i = 0;
		for (Dozent d : dozentList) {
			if(d.getId() == i) {
				dozentList.set(i, dozent);
				break;
			} else {
				i++;
			}
		}
	}
	
	void updateRaum(Raum raum) {
		List<Raum> raumList = raumDataProvider.getList();
		int i = 0;
		for (Raum r : raumList) {
			if(r.getId() == i) {
				raumList.set(i, raum);
				break;
			} else {
				i++;
			}
		}
	}
	
	void updateLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		List<Lehrveranstaltung> lvList = lvDataProvider.getList();
		int i = 0;
		for (Lehrveranstaltung lv : lvList) {
			if(lv.getId() == i) {
				lvList.set(i, lehrveranstaltung);
				break;
			} else {
				i++;
			}
		}
	}
	
	void updateStudiengang(Studiengang studiengang) {
		List<Studiengang> sgList = sgDataProvider.getList();
		int i = 0;
		for (Studiengang sg : sgList) {
			if(sg.getId() == i) {
				sgList.set(i, studiengang);
				break;
			} else {
				i++;
			}
		}
	}
	
	void updateSemesterverband(Semesterverband semesterverband) {
		List<Semesterverband> svList = svDataProvider.getList();
		int i = 0;
		for (Semesterverband sv : svList) {
			if(sv.getId() == i) {
				svList.set(i, semesterverband);
				break;
			} else {
				i++;
			}
		}
	}
	
	void deleteDozent(Dozent dozent) {
		dozentDataProvider.getList().remove(dozent);
		//lvDataProvider.remove(dozent);
	}
	
	void deleteRaum(Raum raum) {
		raumDataProvider.getList().remove(raum);
		//lvDataProvider.remove(dozent);
	}
	
	void deleteLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		lvDataProvider.getList().remove(lehrveranstaltung);
		//lvDataProvider.remove(dozent);
	}
	
	void deleteStudiengang(Studiengang studiengang) {
		sgDataProvider.getList().remove(studiengang);
		//lvDataProvider.remove(dozent);
	}
	
	void deleteSemesterverband(Semesterverband semesterverband) {
		svDataProvider.getList().remove(semesterverband);
		//lvDataProvider.remove(dozent);
	}
	

	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value instanceof String && (String)value == "Root") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Stammdaten");
			stringDataProvider.getList().add("Bewegungsdaten");
			stringDataProvider.getList().add("Report");
			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
			
		}
		
		if (value instanceof String && (String)value == "Stammdaten") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Dozent");
			stringDataProvider.getList().add("Lehrveranstaltung");
			stringDataProvider.getList().add("Semesterverband");
			stringDataProvider.getList().add("Studiengang");
			stringDataProvider.getList().add("Raum");
			stringDataProvider.getList().add("Zeitslot");
			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
			
		}
		
		if (value instanceof String && (String)value == "Bewegungsdaten") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Stundenplaneintrag");
			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
			
		}
		
		if (value instanceof String && (String)value == "Report") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Stundenplan");
			stringDataProvider.getList().add("Raumbelegungsplan");
			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
			
		}
		
		if (value instanceof String && (String)value == "Dozent") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Dozent anlegen");
			stringDataProvider.getList().add("Dozent verwalten");
			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
		}
		
		
		if (value instanceof String && (String)value == "Lehrveranstaltung") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Lehrveranstaltung anlegen");
			stringDataProvider.getList().add("Lehrveranstaltung verwalten");

			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Semesterverband") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Semesterverband anlegen");
			stringDataProvider.getList().add("Semesterverband verwalten");

			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Studiengang") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Studiengang anlegen");
			stringDataProvider.getList().add("Studiengang verwalten");

			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);	
		}
		
		if (value instanceof String && (String)value == "Raum") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Raum anlegen");
			stringDataProvider.getList().add("Raum verwalten");

			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);	
		}
		
		
		if (value instanceof String && (String)value == "Stundenplaneintrag") {
			
			stringDataProvider = new ListDataProvider<String>();
			
			stringDataProvider.getList().add("Stundenplaneintrag anlegen");
			
			return new DefaultNodeInfo<String>(stringDataProvider, new StringCell(), selectionModel, null);	
		}
		
		if (value instanceof String && (String)value=="Dozent verwalten") {
			dozentDataProvider = new ListDataProvider<Dozent>();
			verwaltungsSvc.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
				public void onFailure(Throwable T) {
					
				}
				
				public void onSuccess(Vector<Dozent> dozenten) {
					for (Dozent d : dozenten) {
						dozentDataProvider.getList().add(d);
					}
				}
			});
			
			return new DefaultNodeInfo<Dozent>(dozentDataProvider, new DozentCell(), selectionModel, null);
		}
		
		
		
		if (value instanceof String && (String)value=="Lehrveranstaltung verwalten") {
			lvDataProvider = new ListDataProvider<Lehrveranstaltung>();
			verwaltungsSvc.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
				public void onFailure(Throwable T) {
					
				}
				
				public void onSuccess(Vector<Lehrveranstaltung> lehrveranstaltungen) {
					for (Lehrveranstaltung lv : lehrveranstaltungen) {
						lvDataProvider.getList().add(lv);
					}
				}
			});
			
			return new DefaultNodeInfo<Lehrveranstaltung>(lvDataProvider, new LehrveranstaltungCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value=="Raum verwalten") {
			raumDataProvider = new ListDataProvider<Raum>();
			verwaltungsSvc.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
				public void onFailure(Throwable T) {
					
				}
				
				public void onSuccess(Vector<Raum> raeume) {
					for (Raum r : raeume) {
						raumDataProvider.getList().add(r);
					}
				}
			});
			
			return new DefaultNodeInfo<Raum>(raumDataProvider, new RaumCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value=="Semesterverband verwalten") {
			svDataProvider = new ListDataProvider<Semesterverband>();
			verwaltungsSvc.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
				public void onFailure(Throwable T) {
					
				}
				
				public void onSuccess(Vector<Semesterverband> semesterverbaende) {
					for (Semesterverband sv : semesterverbaende) {
						svDataProvider.getList().add(sv);
					}
				}
			});
			
			return new DefaultNodeInfo<Semesterverband>(svDataProvider, new SemesterverbandCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value=="Studiengang verwalten") {
			sgDataProvider = new ListDataProvider<Studiengang>();
			verwaltungsSvc.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
				public void onFailure(Throwable T) {
					
				}
				
				public void onSuccess(Vector<Studiengang> studiengaenge) {
					for (Studiengang sg : studiengaenge) {
						sgDataProvider.getList().add(sg);
					}
				}
			});
			
			return new DefaultNodeInfo<Studiengang>(sgDataProvider, new StudiengangCell(), selectionModel, null);
		}
		
	
		
		
		return null;
	}
	

	@Override
	public boolean isLeaf(Object value) {
		return false;
	}
	
	

}
