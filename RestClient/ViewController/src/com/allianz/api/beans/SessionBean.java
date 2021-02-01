package com.allianz.api.beans;

import java.io.File;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class SessionBean implements Serializable {
    public SessionBean() {
    }

    private RichInputText fileName;

    public void setFileName(RichInputText fileName) {
        this.fileName = fileName;
    }

    public RichInputText getFileName() {
        return fileName;
    }

    public String download() {
        openNewTab("http://localhost:8080/downloadFile/" + fileName.getValue().toString());
        System.out.println(fileName.getValue().toString());
        return null;
    }

    private void openNewTab(String repUrl) {
        FacesContext context = JSFUtils.getFacesContext();
        String url = repUrl;
        ExtendedRenderKitService erkService =
            Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);
        erkService.addScript(context, "window.open('" + url + "','_blank')");
        System.out.println(url);
    }
    
    public void inputFileValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue() != null){
            UploadedFile uploadFile = (UploadedFile) valueChangeEvent.getNewValue();
            String filename = uploadFile.getFilename();
            System.err.println(filename);
            //Upload File to path- Return actual server path
            File file = FileUploadClient.uploadedFileToFileConverter(uploadFile);
            FileUploadClient.fileUpload("http://localhost:8080/uploadFile", file);
        }
    }
}
