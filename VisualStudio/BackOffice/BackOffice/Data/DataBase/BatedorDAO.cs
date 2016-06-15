using BackOffice.Business;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace BackOffice.Data.DataBase
{
    public class BatedorDAO
    {
      //  public SqlConnection con { get; set; } //= new SqlConnection(b.database);
        public string dbConf { get; set; }
        public BatedorDAO(string conf)
        {
            this.dbConf = conf;
        }


        internal Batedor get(string mail, SqlConnection connection,SqlTransaction tr)
        {
            Batedor b = null;
            DataTable results = new DataTable();

            string queryString = "select * from dbo.batedor Where Email='" + mail + "';";
            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            if (reader.Read())
            {
                var email = reader[0];
                var nome = reader[1];
                var pass = reader[2];
                var horas = reader[3];
                var tot = reader[4];

               b = new Batedor(mail as string, nome as string, pass as string,
                   Int32.Parse(tot.ToString()) , Double.Parse(horas.ToString()));
            }
            reader.Close();
            return b;
        }

        public Batedor get(string mail)
        {
            Batedor b = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr = con.BeginTransaction();
            try
            {
                b = this.get(mail, con,tr);
                tr.Commit();
            }catch(Exception e)
            {
                tr.Rollback();
            }
            finally
            {

                con.Close();
            }
            return b;
        }

        internal List<String> keySet(SqlConnection connection,SqlTransaction tr)
        {
            List<String> r = new List<string>();
            DataTable results = new DataTable();

            string queryString = "select Email from dbo.batedor ;";
            SqlCommand command = new SqlCommand(queryString, connection,tr);
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


        public List<String> keySet()
        {
            List <String> l= null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr =  con.BeginTransaction();
            try
            {
                l = this.keySet(con,tr);
                tr.Commit();
            }catch(Exception e)
            {
                tr.Rollback();
            }
            finally
            {
                con.Close();
            }
            return l;
        }

        public List<Batedor> Values()
        {
            List<Batedor> l = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr = con.BeginTransaction();
            try
            {
                l = this.Values(con,tr);
                tr.Commit();
            }
            catch (Exception e)
            {
                tr.Rollback();
            }
            finally
            {
                con.Close();
            }
            return l;
        }

        internal List<Batedor> Values(SqlConnection connection,SqlTransaction tr)
        {
            List < Batedor > b = new List<Batedor>();
            List<String> k = keySet(connection,tr);
            foreach(string s in k)
            {
                b.Add(this.get(s, connection,tr));
            }
            return b;
        }



        internal Boolean containsKey(string mail, SqlConnection connection,SqlTransaction tr)
        {
            Boolean ret = false;
            Batedor b = this.get(mail, connection,tr);
            ret = (b != null);
            return ret;
        }

        public Boolean containsKey(string mail)
        {
            Boolean b = false;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr = con.BeginTransaction();
            try
            {
                b = this.containsKey(mail,con,tr);
                tr.Commit();
            }
            catch (Exception e)
            {
                tr.Rollback();
            }
            finally
            {
                con.Close();
            }
            return b;
        }



        internal Batedor put(Batedor novo, SqlConnection connection,SqlTransaction tr)
        {
            Batedor b = this.get(novo.email, connection,tr);
            String queryString;
            if (b == null) //inserie
            {
                queryString = String.Format("INSERT INTO dbo.Batedor " +
                    "(Email, Nome, Password, HorasDeReconhecimento, N_Atividades) "+
                    " VALUES " +
                    " ('{0}', '{1}', '{2}', {3}, {4}); ",
                          novo.email,novo.nome,novo.password,novo.ficha.horasEmReConhecimento,novo.ficha.nAtividades);

                
                //INSERT INTO dbo.Batedor
                // (Email, Nome, Password, HorasDeReconhecimento, N_Atividades)
                //VALUES
                //('a@a.pt', 'sandra', '1', 5.5, 1);
            }
            else//update
            {
                // UPDATE dbo.Batedor
                // SET Nome = 'ze', Password = 'novo', HorasDeReconhecimento = 10, N_Atividades = 10
                // WHERE Email = 'a@a.pt';
                queryString = String.Format("UPDATE dbo.Batedor " +
                    " SET Nome = '{0}', Password = '{1}', HorasDeReconhecimento = {2}, N_Atividades = {3} " +
                    " WHERE Email = '{4}' ",
                          novo.nome, novo.password, novo.ficha.horasEmReConhecimento, novo.ficha.nAtividades,novo.email);
            }
            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            command.ExecuteNonQuery();
            return b;
        }


        public Batedor put(Batedor novo)
        {
            Batedor b = null;

            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr =  con.BeginTransaction();
            try
            {
                b = this.put(novo, con,tr);
                tr.Commit();
            }catch(Exception e)
            {
                tr.Rollback();
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
    }
}
