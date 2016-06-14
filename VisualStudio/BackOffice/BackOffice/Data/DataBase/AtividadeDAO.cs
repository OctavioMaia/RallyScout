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
    }
}
