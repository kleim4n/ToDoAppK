/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Project;
import util.ConnectionFactory;

/**
 *
 * @author gklei
 */
public class ProjectController {
    public void save(Project project){
        String sql = "INSERT INTO projects (name, description, createdAt, updatedAt) VALUES (?,?,?,?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setDate(3, new Date(project.getCreatedAt().getTime()));
            statement.setDate(4, new Date(project.getUpdatedAt().getTime()));
            statement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar ProjectController ", ex);
        } finally{
            ConnectionFactory.closeConnection(connection, statement);
        }
    }
    
    public void update(Project project){
        String sql = "UPDATE projects SET name = ?, description = ?, createdAt = ?, updatedAt = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try{
            //Tentar estabelecer a conexão com BD
            connection = ConnectionFactory.getConnection();
            //Preperando a query
            statement = connection.prepareStatement(sql);
            //Set dos valores no statement
            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setDate(3, new Date(project.getCreatedAt().getTime()));
            statement.setDate(4, new Date(project.getUpdatedAt().getTime()));
            statement.setInt(5, project.getId());
            //Executar query
            statement.execute();
        } catch (SQLException ex){
            throw new RuntimeException("Erro ao atualizar  o projeto" + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(connection, statement);
        }
    }
    
    public void removeById(int projectId) throws SQLException{
        String sql =  "DELETE FROM projects WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            //Criar conexão com BD
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, projectId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar  o projeto.");
        } finally{
            ConnectionFactory.closeConnection(conn, statement);
        }
    }
    //?
    public List<Project> getAll(){
        String sql = "SELECT * FROM projects";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultset = null;
        
        List<Project> projects = new ArrayList<>();
        
        try{
            //Criação de conexão
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            //Set do valor correspondente ao filtro de busca
            //statement.setInt(1, idProject);
            //Valor retornado pela execução da query
            resultset = statement.executeQuery();
            //Enquanto houverem valores a serem percorridos no resultset
            while (resultset.next()){
                Project project = new Project();
                project.setId(resultset.getInt("id"));
                project.setName(resultset.getString("name"));
                project.setDescription(resultset.getString("description"));
                project.setCreatedAt(resultset.getDate("createdAt"));
                project.setUpdatedAt(resultset.getDate("updatedAt"));
                projects.add(project);
            }
            
        } catch (SQLException ex){
            throw new RuntimeException("Erro ao buscar os projetos " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(connection, statement, resultset);
        }
        //Lista de tarefas criadas e carrega do BD
        return projects;
    }
}