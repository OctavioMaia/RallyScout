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
            Atividade a = null;
            return a;
        }

    }
}
