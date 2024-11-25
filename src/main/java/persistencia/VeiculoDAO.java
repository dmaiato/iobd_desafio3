
package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import negocio.Veiculo;

/**
 *
 * @author iapereira
 */
public class VeiculoDAO {

    private ConexaoPostgreSQL parametros;

    public VeiculoDAO(String username, String password) throws Exception {
        parametros = new ConexaoPostgreSQL(username, password);
        parametros.getConexao().close();
    }

    public Veiculo obter(int id) throws SQLException {
        Veiculo veiculo = new Veiculo();

        String sql = "SELECT * FROM ONLY veiculo where id = ?;";
        Connection conexao = parametros.getConexao();
        // transformando a string sql em sql "executavel"
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        // o resultado da consulta "cai" em REsult set
        ResultSet rs = preparedStatement.executeQuery();

        // para cada tupla retornada
        if (rs.next()) {
            veiculo.setId(rs.getInt("id"));
            veiculo.setAno(rs.getInt("ano"));
            veiculo.setPlaca(rs.getString("placa"));
        }
        conexao.close();
    
        return veiculo;
    }
    
    public ArrayList<Veiculo> listar() throws SQLException{
        Connection conexao = parametros.getConexao();
        ArrayList<Veiculo> vet = new ArrayList<>();
        
        String sql = "SELECT * FROM ONLY veiculo;";
        // transformando a string sql em sql "executavel"
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        // o resultado da consulta "cai" em Result set
        ResultSet rs = preparedStatement.executeQuery();
        
        // para cada tupla retornada
        while (rs.next()){
            Veiculo veiculo = new Veiculo();
            veiculo.setId(rs.getInt("id"));
            veiculo.setAno(rs.getInt("ano"));
            veiculo.setPlaca(rs.getString("placa"));
            // coloando o novo objeto (tupla) no vetor de objetos de veiculo
            vet.add(veiculo);
        }
        conexao.close();

        return vet;
    }

    public boolean inserir(Veiculo veiculo) throws SQLException {

        String sql = "INSERT INTO veiculo (placa, ano) VALUES(?, ?) RETURNING id;";
        Connection connection = parametros.getConexao();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, veiculo.getPlaca());
        preparedStatement.setInt(2, veiculo.getAno());

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            veiculo.setId(rs.getInt("id"));
        }
        preparedStatement.close();
        connection.close();
        return ((veiculo.getId() != 0) ? true : false);

    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM veiculo WHERE id = ?";
        Connection connection = parametros.getConexao();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, id);
        int linhasAfetadas = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return linhasAfetadas == 1;

    }

    public boolean atualizar(Veiculo v) throws SQLException {
        String sql = "UPDATE veiculo SET placa = ?, ano = ? WHERE id = ?";
        Connection connection = parametros.getConexao();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, v.getPlaca());
        preparedStatement.setInt(2, v.getAno());
        preparedStatement.setInt(3, v.getId());

        int linhasAfetadas = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return linhasAfetadas == 1;
    }
    
    
}
