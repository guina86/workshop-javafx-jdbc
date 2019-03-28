package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;


public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		
		try(PreparedStatement st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)",
			Statement.RETURN_GENERATED_KEYS)) {
			
			st.setString(1, obj.getName());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Unexpected error! no rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void update(Department obj) {
		
		try(PreparedStatement st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?")) {
				
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	

	@Override
	public void deleteById(Integer id) {
		
		try(PreparedStatement st = conn.prepareStatement("DELETE FROM department WHERE Id = ?")) {
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public Department findById(Integer id) {
		
		try(PreparedStatement st = conn.prepareStatement("SELECT department.* FROM department WHERE id = ?")) {
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return new Department(id, rs.getString(2));
			}
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll() {
		
		try(PreparedStatement st = conn.prepareStatement("SELECT department.* FROM department ORDER BY Name")) {
			ResultSet rs = st.executeQuery();
			List<Department> list = new ArrayList<>();
			while(rs.next()) {
				list.add(new Department(rs.getInt(1), rs.getString(2)));
			}
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
}
