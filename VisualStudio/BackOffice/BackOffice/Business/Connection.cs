using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SqlClient;
using System.Windows;

namespace BackOffice.Business
{
    class Connection
    {
        public static SqlConnection GetConnection()
        {
            MessageBox.Show("entrei na classe connection");

            string str = "Data Source=BEASTPC;Initial Catalog=BackOffice;Integrated Security=True";

            SqlConnection con = new SqlConnection(str);
            con.Open();
            MessageBox.Show("ligacao com sucesso");
            return con;
        }
    }
}
