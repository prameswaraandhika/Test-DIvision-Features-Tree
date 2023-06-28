package com.mycompany.formdivisioperasi.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Divisi;
import sql.ConnectionManager;
import com.mycompany.formdivisioperasi.service.FiturDivisiServices;

/**
 *
 * @author Andhika Prameswara <prameswaara@gmail.com>
 */
public class FiturDivisiImpl implements FiturDivisiServices {

    @Override
    public long tambahDivisi(long selectedNodeID, Divisi divisi) {
        String query = "INSERT INTO public.tabel_divisi("
                + "divisi, kode_divisi)\n"
                + "VALUES (?, ?) RETURNING id;";
        long idSub = 0;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try {
            conn = new ConnectionManager().getConnection("data_pelaporan");
            pre = conn.prepareStatement(query);
            pre.setString(1, divisi.getDivisi());
            pre.setString(2, divisi.getKodeDivisi());

            res = pre.executeQuery();

            while (res.next()) {
                idSub = res.getLong("id");
            }

            tambahIdStruktur(selectedNodeID, idSub, conn);
        } catch (Exception e) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeCRP(conn, res, pre);
        }
        return idSub;
    }

    @Override
    public void tambahIdStruktur(long idParent, long idSub, Connection conn) {
        String query = "INSERT INTO public.tabel_divisi_struktur(\n"
                + "	id_parent, id_sub)\n"
                + "	VALUES (?, ?);";
        PreparedStatement pre = null;
        try {
            pre = conn.prepareStatement(query);
            pre.setLong(1, idParent);
            pre.setLong(2, idSub);
            pre.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeCP(conn, pre);
        }
    }

    @Override
    public void editDivisi(Divisi divisi) {
        String query = "UPDATE public.tabel_divisi\n"
                + "	SET divisi=?, kode_divisi=?\n"
                + "	WHERE id=?;";
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = new ConnectionManager().getConnection("data_pelaporan");
            pre = conn.prepareStatement(query);
            System.out.println("ID " + divisi.getId());
            pre.setString(1, divisi.getDivisi());
            pre.setString(2, divisi.getKodeDivisi());
            pre.setLong(3, divisi.getId());
            int result = pre.executeUpdate();
            System.out.println("result insert = " + result);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                pre.close();
            } catch (SQLException ex) {
                Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void hapusDivisi(long id) {
        String query = "DELETE FROM public.tabel_divisi\n"
                + "	WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = new ConnectionManager().getConnection("data_pelaporan");
            pre = conn.prepareStatement(query);
            pre.setLong(1, id);
            pre.executeQuery();
        } catch (Exception e) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeCP(conn, pre);
        }
    }

    @Override
    public List<Divisi> ambilDataParent() {
        List<Divisi> parentDivisi = new ArrayList<>();
        String query = "SELECT *\n"
                + "FROM public.tabel_divisi\n"
                + "WHERE id NOT IN (SELECT id_sub FROM tabel_divisi_struktur);";
        Connection conn = null;
        ResultSet res = null;
        PreparedStatement pre = null;
        try {
            conn = new ConnectionManager().getConnection("data_pelaporan");
            pre = conn.prepareStatement(query);
            res = pre.executeQuery();
            while (res.next()) {
                parentDivisi.add(
                        new Divisi(
                                res.getLong("id"),
                                res.getString("divisi"),
                                res.getString("kode_divisi")));
            }
        } catch (Exception e) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeCRP(conn, res, pre);
        }
        return parentDivisi;
    }

    @Override
    public List<Divisi> ambilDataDariParentID(long parentID) {
        String query = "SELECT *\n"
                + "FROM tabel_divisi\n"
                + "WHERE id IN (\n"
                + "	SELECT id_sub \n"
                + "	FROM tabel_divisi_struktur \n"
                + "	WHERE id_parent = ?);";

        List<Divisi> child = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        try {
            conn = new ConnectionManager().getConnection("data_pelaporan");
            pre = conn.prepareStatement(query);
            pre.setLong(1, parentID);
            res = pre.executeQuery();

            while (res.next()) {
                child.add(new Divisi(res.getLong("id"), res.getString("divisi"), res.getString("kode_divisi")));
            }
        } catch (Exception ex) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeCRP(conn, res, pre);
        }

        return child;
    }

    private void closeCRP(Connection conn, ResultSet res, PreparedStatement pre) {
        try {
            if (res != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (res != null) {
                pre.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeCP(Connection conn, PreparedStatement pre) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            pre.close();
        } catch (SQLException ex) {
            Logger.getLogger(FiturDivisiImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
