package com.example.roomify;

//Root is responsible for an object that contains the schedule export results obtained from the API and provides access to these results through access methods
public class Root {
    private PsrozkladExport psrozklad_export;

    public PsrozkladExport getPsrozklad_export() {
        return psrozklad_export;
    }

    public void setPsrozklad_export(PsrozkladExport psrozklad_export) {
        this.psrozklad_export = psrozklad_export;
    }
}
