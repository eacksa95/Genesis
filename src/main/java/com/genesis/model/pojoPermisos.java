
package com.genesis.model;

/**
 *
 * @author RC
 */
public class pojoPermisos {
    private String menuid;
    private String menutext;
    private int ver;
    private int C;
    private int R;
    private int U;
    private int D;
  
    public pojoPermisos() {
    
} 
    public pojoPermisos(String menuid, String menutext, int ver, int C, int R, int U, int D) {
        this.menuid = menuid;
        this.menutext = menutext;
        this.ver = ver;
        this.C = C;
        this.R = R;
        this.U = U;
        this.D = D;
    }
         //===================== GETTERS =======================================
    public String getString(String arg){
        switch(arg){
            case "menuid" :
                return this.menuid;
                //break;
            default :
                return "";
        }
    }

    public int getInteger(String arg){
        switch(arg){
            case "ver" :
                return this.ver;
                //break;
            case "C" :
                return this.C;
                //break;
            case "R" :
                return this.R;
                //break;
            case "U" :
                return this.U;
                //break;
            case "D" :
                return this.D;
                //break;
            default :
                return 0;
        }
    }

    

    //===================== SETTERS =======================================
    public void setString(String attribute, String value){
        switch(attribute){
            case "menuid" :
                this.menuid = value;
                break;
            case "menutext" :
                this.menutext = value;
                break; 
        }
    }

    public void setInteger(String attribute, int value){
        switch(attribute){
            case "ver" :
                this.ver = value;
                //break;
             case "C" :
                this.C = value;
                //break;
             case "R" :
                this.R = value;
                //break;
             case "U" :
                this.U = value;
                //break;
             case "D" :
                this.D = value;
                //break;
             
        }
    }

   
    //=============================================
    
}


