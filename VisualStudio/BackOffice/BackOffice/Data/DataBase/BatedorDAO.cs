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
    class BatedorDAO
    {
      //  public SqlConnection con { get; set; } //= new SqlConnection(b.database);
        public string dbConf { get; set; }
        public BatedorDAO(string conf)
        {
            this.dbConf = conf;
        }


        private Batedor get(string mail, SqlConnection connection)
        {
            Batedor b = null;
            DataTable results = new DataTable();

            string queryString = "select * from dbo.batedor Where Email='" + mail + "';";
            SqlCommand command = new SqlCommand(queryString, connection);
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

            return b;
        }

        public Batedor get(string mail)
        {
            Batedor b = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            try
            {
                b = this.get(mail, con);
            }
            finally
            {

                con.Close();
            }
            return b;
        }

        private List<String> keySet(SqlConnection connection)
        {
            List<String> r = new List<string>();
            DataTable results = new DataTable();

            string queryString = "select Email from dbo.batedor ;";
            SqlCommand command = new SqlCommand(queryString, connection);
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var email = reader[0];


                r.Add(email as string);

            }

            return r;
        }


        public List<String> keySet()
        {
            List <String> l= null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
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

        public List<Batedor> Values()
        {
            List<Batedor> l = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
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

        public List<Batedor> Values(SqlConnection connection)
        {
            List < Batedor > b = new List<Batedor>();
            List<String> k = keySet(connection);
            foreach(string s in k)
            {
                b.Add(this.get(s, connection));
            }
            return b;
        }


    }
}
