package com.mycompany.formdivisioperasi;

import com.mycompany.formdivisioperasi.service.FiturDivisiImpl;
import com.mycompany.formdivisioperasi.service.FiturDivisiServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Divisi;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Andhika Prameswara <prameswaara@gmail.com>
 */
@ViewScoped
@ManagedBean
public class DivisiMBean implements Serializable {

    private int noDiv = 1;
    private int id;
    private String divisi;
    private String kode;
    private TreeNode<Divisi> selectedNode;
    private TreeNode<Divisi> root;
    private List<Divisi> sub;
    private TreeNode<Divisi> tempNode;
    private List<Divisi> child;
    private Divisi divisiModel;
    private FiturDivisiServices services;
    private boolean isProtected = false;

    @PostConstruct
    void init() {
        services = new FiturDivisiImpl();
        root = new DefaultTreeNode<>(new Divisi(0, "", ""), null);
        sub = new ArrayList<>();
        tempNode = new DefaultTreeNode<>();
        new DefaultTreeNode<>(new Divisi(1, "Divisi", "-"), root);

    }

    public void tambahNode() {
        this.divisiModel = new Divisi(id, divisi, divisi.substring(0, 2).toUpperCase() + noDiv++);
        new DefaultTreeNode<>(divisiModel, selectedNode);
        services.tambahDivisi(selectedNode.getData().getId(), divisiModel);
    }

    public void hapusNode() {
        long selectedNodeID = this.selectedNode.getData().getId();
        this.selectedNode.getChildren().clear();
        this.selectedNode.getParent().getChildren().remove(selectedNode);
        this.selectedNode = null;
        services.hapusDivisi(selectedNodeID);
    }

    public void editNode() {
        this.divisiModel = new Divisi(id, divisi, divisi.substring(0, 2).toUpperCase() + noDiv++);
        services.editDivisi(new Divisi(selectedNode.getData().getId(), divisi, divisi.substring(0, 2).toUpperCase() + noDiv++));
        new DefaultTreeNode(divisiModel, selectedNode.getParent());
        this.selectedNode.getParent().getChildren().remove(selectedNode);
    }

    public void onNodeExpand() {
        List<Divisi> child;

        selectedNode.getChildren()
                .clear();
        long parentId = selectedNode.getData().getId();
        child = services.ambilDataDariParentID(parentId);
        for (Divisi divisi1 : sub) {
            System.out.println("Exp Nama divisi: " + divisi1.getDivisi());
        }

        if (child.size() >= 1) {
            for (Divisi node : child) {
                new DefaultTreeNode<>(node, selectedNode);
            }
        }
    }

    public void onNodeCollapse() {
        selectedNode.getChildren().clear();
    }

    public void onNodeSelect() {

        if (selectedNode != null) {
            this.divisi = selectedNode.getData().getDivisi();
            isProtected = divisi.equalsIgnoreCase("Divisi");
        }

        long parentId = selectedNode.getData().getId();
        child = services.ambilDataDariParentID(parentId);
        if (child.size() >= 1) {
            tempNode = new DefaultTreeNode<>(new Divisi(0, "", ""), selectedNode);
        }

        if (selectedNode.isExpanded()) {
            selectedNode.getChildren().remove(tempNode);
        }

    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public TreeNode<Divisi> getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode<Divisi> selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode<Divisi> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<Divisi> root) {
        this.root = root;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public TreeNode<Divisi> getTempNode() {
        return tempNode;
    }

    public void setTempNode(TreeNode<Divisi> tempNode) {
        this.tempNode = tempNode;
    }

    public List<Divisi> getSub() {
        return sub;
    }

    public void setSub(List<Divisi> sub) {
        this.sub = sub;
    }

    public List<Divisi> getChild() {
        return child;
    }

    public void setChild(List<Divisi> child) {
        this.child = child;
    }

    public boolean isIsProtected() {
        return isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

}
