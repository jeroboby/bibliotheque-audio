package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;

public class DisqueDAO {
	private Connection connection;
	private ChansonDAO chansonDAO;

	public DisqueDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque_audio", "root", "");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Chargement driver failure", e);
		} catch (SQLException e) {
			throw new RuntimeException("Impossible d'établir une connection avec le SGBD", e);
		}
	}

	public Disque findById(String id) {
		try {
			String sql = "SELECT * FROM `disques` WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return createDisqueFromResultSet(resultSet);
			else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public List<Disque> findAll() {
		try {
			List<Disque> disques = new ArrayList<>();

			String sql = "SELECT * FROM `disques`";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Disque disque = createDisqueFromResultSet(resultSet);
				disques.add(disque);
			}

			return disques;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public List<Disque> findByName(String search) {
		search = search.toLowerCase();

		try {
			List<Disque> disques = new ArrayList<>();

			String sql = "SELECT * FROM `disques` WHERE LOWER(`nom`) LIKE ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, "%" + search + "%");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Disque disque = createDisqueFromResultSet(resultSet);
				disques.add(disque);
			}

			return disques;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public Disque addDisque(Disque disque) {
		try {
			String sql = "insert into `disques` (id, nom) values (?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, disque.getCodeBarre());
			statement.setString(2, disque.getNom());

			int result = statement.executeUpdate();
			if (result == 1) {
				return disque;
			} else {
				System.out.println("Probleme lors de l'ajout du disque");
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}

	}

	public void update(Disque disque) {
		if (disque.getCodeBarre() == null) {
			System.out.println("ATTENTION, CE N'EST PAS UNE MAJ MAIS UN INSERT !");
			addDisque(disque);
			return;
		}
		try {

			String sql = "update `disques` set `nom` = ? where `id` = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, disque.getNom());
			statement.setString(2, disque.getCodeBarre());

			int result = statement.executeUpdate();

			for (Chanson chanson : disque.getChansons()) {
				if (chanson.getId() <= 0)
					chansonDAO.add(chanson);
				else
					chansonDAO.update(chanson);
			}
			for (Chanson chanson : chansonDAO.findByDisqueId(disque.getCodeBarre())) {
				if (!doesDisqueHasChanson(disque, chanson.getId()))
					chansonDAO.delete(chanson.getId());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public void delete(String id) {
		try {
			// effacer toutes les chansons du disque puisqu'on efface le disque
			chansonDAO.deleteByDisqueId(id);

			String sql = "DELETE FROM disques WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de retirer le disque", e);
		}
	}

	private boolean doesDisqueHasChanson(Disque disque, int chansonId) {
		for (Chanson chanson : disque.getChansons()) {
			if (chanson.getId() == chansonId)
				return true;
		}

		return false;
	}

	private Disque createDisqueFromResultSet(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String nom = resultSet.getString("nom");

		Disque disque = new Disque();
		disque.setCodeBarre(id);
		disque.setNom(nom);

		List<Chanson> chansons = chansonDAO.findByDisqueId(id);
		for (Chanson chanson : chansons)
			disque.addChanson(chanson);

		return disque;
	}

}
