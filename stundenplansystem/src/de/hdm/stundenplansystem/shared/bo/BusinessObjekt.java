package de.hdm.stundenplansystem.shared.bo;

import java.io.Serializable;

//author:thies & holz

public class BusinessObjekt implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String toString() {
		return this.getClass().getName() + "_" + this.id;
	}
	
	public boolean equals(Object o) {
	    if (o != null && o instanceof BusinessObjekt) {
	      BusinessObjekt a = (BusinessObjekt) o;
	      try {
	        if (a.getId() == this.id)
	          return true;
	      }
	      catch (IllegalArgumentException e) {
	    	  return false;
	      }
	    }
	    return false;
	  }
	
	 public int hashCode() {
		  return this.id;
	  }
	
}

