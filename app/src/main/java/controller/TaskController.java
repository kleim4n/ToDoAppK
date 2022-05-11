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
import model.Task;
import util.ConnectionFactory;

/**
 *
 * @author gklei
 */
public class TaskController {
    public void save(Task task){
        String sql = "INSERT INTO tasks (idProject, name, description, completed, notes, deadline, createdAt, updatedAt) VALUES (?,?,?,?,?,?,?,?,?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, task.getIdProject());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setBoolean(4, task.isIsCompleted());
            statement.setString(5, task.getNotes());
            statement.setDate(6, new Date(task.getDeadline().getTime()));
            statement.setDate(7, new Date(task.getCreatedAt().getTime()));
            statement.setDate(8, new Date(task.getUpdatedAt().getTime()));
            statement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar tarefa" + ex.getMessage());
        } finally{
            ConnectionFactory.closeConnection(connection, statement);
        }
    }
    
    public void update(Task task){
        String sql = "UPDATE tasks SET idProject = ?, name = ?, description = ?, notes = ?, completed = ?, deadline = ?, createdAt = ?, updatedAt = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try{
            //Tentar estabelecer a conexão com BD
            connection = ConnectionFactory.getConnection();
            //Preperando a query
            statement = connection.prepareStatement(sql);
            //Set dos valores no statement
            statement.setInt(1, task.getIdProject());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setString(4, task.getNotes());
            statement.setBoolean(5, task.isIsCompleted());
            statement.setDate(6, new Date(task.getDeadline().getTime()));
            statement.setDate(7, new Date(task.getCreatedAt().getTime()));
            statement.setDate(8, new Date(task.getUpdatedAt().getTime()));
            statement.setInt(9, task.getId());
            //Executar query
            statement.execute();
        } catch (Exception ex){
            throw new RuntimeException("Erro ao atualizar a tarefa" + ex.getMessage(), ex);
        }
    }
    
    public void removeById(int taskId) throws SQLException{
        String sql =  "DELETE FROM tasks WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            //Criar conexão com BD
            conn = ConnectionFactory.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, taskId);
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar a tarefa.");
        } finally{
            ConnectionFactory.closeConnection(conn, statement);
        }
    }
    
    public List<Task> getAll(int idProject){
        String sql = "SELECT * FROM tasks WHERE idProject = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultset = null;
        
        List<Task> tasks = new ArrayList<Task>();
        
        try{
            //Criação de conexão
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            //Set do valor correspondente ao filtro de busca
            statement.setInt(1, idProject);
            //Valor retornado pela execução da query
            resultset = statement.executeQuery();
            //Enquanto houverem valores a serem percorridos no resultset
            while (resultset.next()){
                Task task = new Task();
                task.setId(resultset.getInt("id"));
                task.setIdProject(resultset.getInt("idProject"));
                task.setName(resultset.getString("name"));
                task.setDescription(resultset.getString("description"));
                task.setNotes(resultset.getString("notes"));
                task.setIsCompleted(resultset.getBoolean("completed"));
                task.setDeadline(resultset.getDate("deadline"));
                task.setCreatedAt(resultset.getDate("createdAt"));
                task.setUpdatedAt(resultset.getDate("updateAt"));
                tasks.add(task);
            }
            
        } catch (Exception ex){
            throw new RuntimeException("Erro ao buscar as tarefas " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(connection, statement, resultset);
        }
        //Lista de tarefas criadas e carrega do BD
        return tasks;
    }
}
