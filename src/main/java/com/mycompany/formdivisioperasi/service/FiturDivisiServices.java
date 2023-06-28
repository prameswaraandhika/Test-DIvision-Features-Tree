package com.mycompany.formdivisioperasi.service;

import java.sql.Connection;
import java.util.List;
import model.Divisi;

/**
 *
 * @author Andhika Prameswara <prameswaara@gmail.com>
 */
public interface FiturDivisiServices {

    public long tambahDivisi(long selectedNodeID, Divisi divisi);

    public void tambahIdStruktur(long idParent, long idSub, Connection conn);

    public void editDivisi(Divisi divisi);

    public void hapusDivisi(long id);

    public List<Divisi> ambilDataParent();

    public List<Divisi> ambilDataDariParentID(long parentId);
}
