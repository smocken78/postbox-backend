package org.mocken.database.statements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.database.connectors.ConnectionManager;
import org.mocken.tools.HashGenerator;
import org.mocken.user.User;

public class LoginStatements {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private ConnectionManager cm;

	public LoginStatements() {
		try {
			cm = new ConnectionManager("msports");
		} 
		catch (NamingException e) {
			// TODO Auto-generated catch block
			logger.error("This should never happen if your jdbc-datasources configuration is alright",e);
		}
	}
	
	
	public User validateLoginData(String email,String password,String trainer_id) {
		
		Connection con = null;
		User user = null;
		try {
			con = cm.getConnection();
			long coach = Long.parseLong(trainer_id);
			PreparedStatement ps = con.prepareStatement("select us.*, up.profile_picture_link,up.street,up.zip_code,up.city,up.country,up.user_equipment,"
					+ " cp.company_id,cp.company_name,cp.company_logo,cp.company_configuration, JSON_ARRAYAGG(s2c.company_subscription_id) as company_subscriptions"
					+ " from users us"
					+ " left join user_profiles up on us.user_id = up.user_id"
					+ " left join user2company u2c on us.user_id = u2c.user_id"
					+ " left join companies cp on u2c.company_id = cp.company_id"
					+ " left join subscriptions2company s2c on u2c.company_id = s2c.company_id"
					+ " where us.email = ? and us.trainer_id = ?");
			ps.setString(1, email);
			ps.setLong(2, coach);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (HashGenerator.createSHA1PasswordHash(password).matches(rs.getString("password")) ) {
					user = new User(rs.getLong("user_id"), email,rs.getLong("trainer_id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					user.setProfilePictureLink(rs.getString("profile_picture_link"));
					user.setStreet(rs.getString("street"));
					user.setZipCode(rs.getString("zip_code"));
					user.setCity(rs.getString("city"));
					user.setCountry(rs.getString("country"));
					user.setEquipment(rs.getString("user_equipment"));
					if (rs.getObject("company_id")!=null) {
						user.setCompany(rs.getLong("company_id"),rs.getString("company_name"),
								rs.getString("company_logo"),rs.getString("company_subscriptions"),
								rs.getString("company_configuration"));
					}
				}
			}
			rs.close();
			ps.close();
			
		} 
		catch (Exception e) {
			// we catch any exception
			logger.error("",e);
		} 
		finally {
			if (cm!=null&&con!=null)
				cm.closeConnection(con);
		}
		
		return user;
	}
	
	
	public User apiLogin(String email,long trainer_id) {
		
		Connection con = null;
		User user = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select us.*, up.profile_picture_link,up.street,up.zip_code,up.city,up.country,up.user_equipment,");
		sb.append(" cp.company_id,cp.company_name,cp.company_logo,cp.company_configuration,");
		sb.append(" JSON_ARRAYAGG(s2c.company_subscription_id) as company_subscriptions");
		sb.append(" from users us");
		sb.append(" left join user_profiles up on us.user_id = up.user_id");
		sb.append(" left join user2company u2c on us.user_id = u2c.user_id");
		sb.append(" left join companies cp on u2c.company_id = cp.company_id");
		sb.append(" left join subscriptions2company s2c on u2c.company_id = s2c.company_id");
		sb.append(" where us.email = ? and us.trainer_id = ?");
			
		try {
			con = cm.getConnection();
			PreparedStatement ps = con.prepareStatement(sb.toString());
			ps.setString(1, email);
			ps.setLong(2, trainer_id);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getObject("user_id")!=null) {
					user = new User(rs.getLong("user_id"), email,rs.getLong("trainer_id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					user.setProfilePictureLink(rs.getString("profile_picture_link"));
					user.setStreet(rs.getString("street"));
					user.setZipCode(rs.getString("zip_code"));
					user.setCity(rs.getString("city"));
					user.setCountry(rs.getString("country"));
					user.setEquipment(rs.getString("user_equipment"));
					if (rs.getObject("company_id")!=null) {
						user.setCompany(rs.getLong("company_id"),rs.getString("company_name"),
								rs.getString("company_logo"),rs.getString("company_subscriptions"),
								rs.getString("company_configuration"));
					}
				}
			}
			rs.close();
			ps.close();
			
		} 
		catch (Exception e) {
			// we catch any exception
			logger.error("",e);
		} 
		finally {
			if (cm!=null&&con!=null)
				cm.closeConnection(con);
		}
		
		return user;
	}
	
}
