using BackOffice.Business;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Data.DataBase
{
    public class AtividadeDAO
    {
        public string dbConf { get; set; }
        public AtividadeDAO(string conf)
        {
            this.dbConf = conf;
        }



        public List<int> keySet()
        {
            List<int> l = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            //con.BeginTransaction();
            try
            {
                l = this.keySet(con);
            }
            finally
            {
                con.Close();
            }
            return l;
        }


        public Atividade get(int id)
        {
            Atividade a = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            try
            {
                a = this.get(id, con);
            }
            finally
            {

                con.Close();
            }
            return a;
        }

        public List<Atividade> Values()
        {
            List<Atividade> l = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            // con.BeginTransaction();
            try
            {
                l = this.Values(con);
            }
            finally
            {

                con.Close();
            }
            return l;
        }

        public Boolean containsKey(int id)
        {
            Boolean b = false;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            //con.BeginTransaction();
            try
            {
                b = this.containsKey(id, con);
            }
            finally
            {
                con.Close();
            }
            return b;
        }

        public int size()
        {
            return this.keySet().Count;
        }

        public Atividade put(Atividade novo)
        {
            Atividade b = null;

            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            //con.BeginTransaction();
            try
            {
                b = this.put(novo, con);
            }
            finally
            {
                con.Close();
            }
            return b;
        }

        private List<int> keySet(SqlConnection connection)
        {
            List<int> r = new List<int>();
            DataTable results = new DataTable();

            string queryString = "select id_Atividade from dbo.Atividade ;";
            SqlCommand command = new SqlCommand(queryString, connection);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var id = reader[0];


                r.Add(Int32.Parse(id.ToString()));

            }
            reader.Close();
            return r;
        }


        private Boolean containsKey(int id, SqlConnection connection)
        {
            Boolean ret = false;
            Atividade b = this.get(id, connection);
            ret = (b != null);
            return ret;
        }

        private List<Atividade> Values(SqlConnection connection)
        {
            List<Atividade> b = new List<Atividade>();
            List<int> k = this.keySet(connection);
            foreach (int i in k)
            {
                b.Add(this.get(i, connection));
            }
            return b;
        }

        //TODO daqui para baixo

        private Atividade put(Atividade novo, SqlConnection connection)
        {
            Atividade b = this.get(novo.idAtividade, connection);
            String queryString=null;
            if (b == null) //inserie
            {
                
            }
            else//update
            {
                
            }
            SqlCommand command = new SqlCommand(queryString, connection);
            command.CommandTimeout = 60;
            command.ExecuteNonQuery();
            return b;
        }


        private Atividade get(int id, SqlConnection connection)
        {

            VeiculosDAO vd = new VeiculosDAO(id);
            Atividade a = null;

            List<Veiculo> veiculos = vd.Values(connection);



            return a;
            


        }

    }
    internal class VeiculosDAO
    {
      //  public string dbConf { get; set; }
        public int idatividade { get; set; }
        public VeiculosDAO( int atividade)
        {
         //   this.dbConf = conf;
            this.idatividade = atividade;
        }

        internal Veiculo get(string chassi, SqlConnection connection)
        {
            Veiculo b = null;
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT * dbo.Veiculo " +
                    "WHERE Chassi = '{0}' AND Atividade = {1};",
                          chassi,this.idatividade);

            SqlCommand command = new SqlCommand(queryString, connection);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var chass = reader[0];
                var marca = reader[1];
                var modelo = reader[2];
                int ativ = this.idatividade;

                List<string> caract = new List<string>();
                string queryStringCarac = String.Format("SELECT * dbo.VeiculoCaracteristicas " +
                    "WHERE Chassi = '{0}';",
                          chass);

                SqlCommand commandVC = new SqlCommand(queryStringCarac, connection);
                commandVC.CommandTimeout = 60;
                SqlDataReader readerVC = command.ExecuteReader();
                while (readerVC.Read())
                {
                    caract.Add(reader[0] as string);
                }
                readerVC.Close();

                b = new Veiculo(modelo as string, marca as string, chass as string, caract);
            }
            reader.Close();
            return b;
        }

        internal List<String> keySet(SqlConnection connection)
        {
            List<String> r = new List<string>();
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT Chassi from dbo.Veiculo WHERE Atividade = {0} ;",
                          this.idatividade);
            SqlCommand command = new SqlCommand(queryString, connection);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var email = reader[0];


                r.Add(email as string);

            }
            reader.Close();
            return r;
        }

        internal List<Veiculo> Values(SqlConnection connection)
        {
            List<Veiculo> b = new List<Veiculo>();
            List<String> k = this.keySet(connection);
            foreach (string s in k)
            {
                b.Add(this.get(s, connection));
            }
            return b;
        }


        internal Veiculo put(Veiculo novo, SqlConnection connection)
        {
            Veiculo v = this.get(novo.chassi, connection);
            String queryString;
            if (v == null) //inserie
            {
                queryString = String.Format("INSERT INTO dbo.Veiculo " +
                    "(Chassi, Marca, Modelo, Atividade) " +
                    " VALUES " +
                    " ('{0}', '{1}', '{2}', {3}); ",
                          novo.chassi,novo.marca,novo.modelo,this.idatividade);

                SqlCommand command = new SqlCommand(queryString, connection);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                List<String> cara = novo.caracteristicas;
                String queryStringC;
                foreach(string c in cara)
                {
                    queryStringC = String.Format("INSERT INTO dbo.VeiculoCaracteristicas " +
                    "(Caracteristica, Chassi) " +
                    " VALUES " +
                    " ('{0}', '{1}'); ",
                          c,novo.chassi);
                    command = new SqlCommand(queryString, connection);
                    command.CommandTimeout = 60;
                    command.ExecuteNonQuery();
                }
            }
            else//update
            {
                // UPDATE dbo.Batedor
                // SET Nome = 'ze', Password = 'novo', HorasDeReconhecimento = 10, N_Atividades = 10
                // WHERE Email = 'a@a.pt';
                queryString = String.Format("UPDATE dbo.VeiculoCaracteristicas " +
                    " SET Marca = '{0}', Modelo = '{1}' " +
                    " WHERE Chassi = '{3}'  AND Atividade = {4} ;",
                          novo.marca, novo.modelo, novo.chassi, this.idatividade);
                SqlCommand command = new SqlCommand(queryString, connection);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                //esta update aos veiculos
                List<String> cara = novo.caracteristicas;
                String queryStringC;
                foreach (string c in cara)
                {

                    string queryStringCarac = String.Format("SELECT * dbo.VeiculoCaracteristicas " +
                    "WHERE Chassi = '{0}' AND Caracteristica = '{1}';",
                          novo.chassi, c);

                    SqlCommand commandVC = new SqlCommand(queryStringCarac, connection);
                    commandVC.CommandTimeout = 60;
                    SqlDataReader readerVC = command.ExecuteReader();
                    if (!readerVC.Read())
                    {//novo
                        queryStringC = String.Format("INSERT dbo.VeiculoCaracteristicas " +
                            "(Caracteristica, Chassi) " +
                            " VALUES " +
                            " ('{0}', '{1}'); ",
                          c, novo.chassi);
                        command = new SqlCommand(queryString, connection);
                        command.CommandTimeout = 60;
                        command.ExecuteNonQuery();
                    } 
                    
                }
            }
            return v;
        }

    }


    internal class MapaDAO
    {
        internal Mapa get(int id, SqlConnection connection)
        {
            Veiculo b = null;
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT * dbo.Veiculo " +
                    "WHERE Chassi = '{0}' AND Atividade = {1};",
                          chassi, this.idatividade);

            SqlCommand command = new SqlCommand(queryString, connection);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var chass = reader[0];
                var marca = reader[1];
                var modelo = reader[2];
                int ativ = this.idatividade;

                List<string> caract = new List<string>();
                string queryStringCarac = String.Format("SELECT * dbo.VeiculoCaracteristicas " +
                    "WHERE Chassi = '{0}';",
                          chass);

                SqlCommand commandVC = new SqlCommand(queryStringCarac, connection);
                commandVC.CommandTimeout = 60;
                SqlDataReader readerVC = command.ExecuteReader();
                while (readerVC.Read())
                {
                    caract.Add(reader[0] as string);
                }
                readerVC.Close();

                b = new Veiculo(modelo as string, marca as string, chass as string, caract);
            }
            reader.Close();
            return b;
        }

        internal List<int> keySet(SqlConnection connection)
        {
            List<int> r = new List<int>();
            DataTable results = new DataTable();

            string queryString = "SELECT idMapa from dbo.Mapa;";
            
            SqlCommand command = new SqlCommand(queryString, connection);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var id = reader[0];


                r.Add(Int32.Parse(id.ToString()));

            }
            reader.Close();
            return r;
        }

        internal List<Mapa> Values(SqlConnection connection)
        {
            List<Mapa> b = new List<Mapa>();
            List<int> k = this.keySet(connection);
            foreach (int s in k)
            {
                b.Add(this.get(s, connection));
            }
            return b;
        }


        internal Veiculo put(Veiculo novo, SqlConnection connection)
        {
            Veiculo v = this.get(novo.chassi, connection);
            String queryString;
            if (v == null) //inserie
            {
                queryString = String.Format("INSERT INTO dbo.Veiculo " +
                    "(Chassi, Marca, Modelo, Atividade) " +
                    " VALUES " +
                    " ('{0}', '{1}', '{2}', {3}); ",
                          novo.chassi, novo.marca, novo.modelo, this.idatividade);

                SqlCommand command = new SqlCommand(queryString, connection);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                List<String> cara = novo.caracteristicas;
                String queryStringC;
                foreach (string c in cara)
                {
                    queryStringC = String.Format("INSERT INTO dbo.VeiculoCaracteristicas " +
                    "(Caracteristica, Chassi) " +
                    " VALUES " +
                    " ('{0}', '{1}'); ",
                          c, novo.chassi);
                    command = new SqlCommand(queryString, connection);
                    command.CommandTimeout = 60;
                    command.ExecuteNonQuery();
                }
            }
            else//update
            {
                // UPDATE dbo.Batedor
                // SET Nome = 'ze', Password = 'novo', HorasDeReconhecimento = 10, N_Atividades = 10
                // WHERE Email = 'a@a.pt';
                queryString = String.Format("UPDATE dbo.VeiculoCaracteristicas " +
                    " SET Marca = '{0}', Modelo = '{1}' " +
                    " WHERE Chassi = '{3}'  AND Atividade = {4} ;",
                          novo.marca, novo.modelo, novo.chassi, this.idatividade);
                SqlCommand command = new SqlCommand(queryString, connection);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                //esta update aos veiculos
                List<String> cara = novo.caracteristicas;
                String queryStringC;
                foreach (string c in cara)
                {

                    string queryStringCarac = String.Format("SELECT * dbo.VeiculoCaracteristicas " +
                    "WHERE Chassi = '{0}' AND Caracteristica = '{1}';",
                          novo.chassi, c);

                    SqlCommand commandVC = new SqlCommand(queryStringCarac, connection);
                    commandVC.CommandTimeout = 60;
                    SqlDataReader readerVC = command.ExecuteReader();
                    if (!readerVC.Read())
                    {//novo
                        queryStringC = String.Format("INSERT dbo.VeiculoCaracteristicas " +
                            "(Caracteristica, Chassi) " +
                            " VALUES " +
                            " ('{0}', '{1}'); ",
                          c, novo.chassi);
                        command = new SqlCommand(queryString, connection);
                        command.CommandTimeout = 60;
                        command.ExecuteNonQuery();
                    }

                }
            }
            return v;
        }

    }
}
