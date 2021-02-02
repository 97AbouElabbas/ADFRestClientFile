package com.allianz.api.beans;

import java.io.File;
import java.io.Serializable;

import java.util.Arrays;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class SessionBean implements Serializable {
    private String path;
    private RichImage image;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

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
        if (valueChangeEvent.getNewValue() != null) {
            UploadedFile uploadFile = (UploadedFile) valueChangeEvent.getNewValue();
            String filename = uploadFile.getFilename();
            System.err.println(filename);
            //Upload File to path- Return actual server path
            File file = FileUploadClient.uploadedFileToFileConverter(uploadFile);
            FileUploadClient.fileUpload("http://localhost:8080/uploadFile", file);
        }
    }

    public void imageInputValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null) {
            UploadedFile uploadFile = (UploadedFile) valueChangeEvent.getNewValue();
            String filename = uploadFile.getFilename();
            //Check file extension
            String FileExtension = FileUploadClient.getFileExtn(filename);
            String[] extensions = { "tiff", "jpeg", "gif", "jpg", "png", "pdf" };
            System.err.println(filename);
            if (Arrays.asList(extensions).contains(FileExtension)) {
                //Upload File to path- Return actual server path
                File file = FileUploadClient.uploadedFileToFileConverter(uploadFile);
                FileUploadClient.fileUpload("http://localhost:8080/uploadFile", file);
                this.setPath(filename);
                AdfFacesContext.getCurrentInstance().addPartialTarget(getImage());
            }
        }
    }

    public void setImage(RichImage image) {
        this.image = image;
    }

    public RichImage getImage() {
        return image;
    }
}
