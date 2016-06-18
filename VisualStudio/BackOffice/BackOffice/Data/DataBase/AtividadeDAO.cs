using BackOffice.Business;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Device.Location;
using System.Drawing;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

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


        public Atividade get(int id)
        {
            Atividade a = null;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr = con.BeginTransaction();
            try
            {
                a = this.get(id, con,tr);
                tr.Commit();
            }catch(Exception e)
            {
                tr.Rollback();
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
            SqlTransaction tr =  con.BeginTransaction();
            try
            {
                l = this.Values(con,tr);
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

        public Boolean containsKey(int id)
        {
            Boolean b = false;
            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction tr =  con.BeginTransaction();
            try
            {
                b = this.containsKey(id, con,tr);
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


        //TODO VER aui
        public Atividade put(Atividade novo)
        {
            Atividade b = null;

            SqlConnection con = new SqlConnection(this.dbConf);
            con.Open();
            SqlTransaction  tr = con.BeginTransaction();

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

        private List<int> keySet(SqlConnection connection,SqlTransaction tr)
        {
            List<int> r = new List<int>();
            DataTable results = new DataTable();

            string queryString = "select id_Atividade from dbo.Atividade ;";
            SqlCommand command = new SqlCommand(queryString, connection,tr);
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


        private Boolean containsKey(int id, SqlConnection connection , SqlTransaction tr)
        {
            Boolean ret = false;
            Atividade b = this.get(id, connection,tr);
            ret = (b != null);
            return ret;
        }

        private List<Atividade> Values(SqlConnection connection, SqlTransaction tr)
        {
            List<Atividade> b = new List<Atividade>();
            List<int> k = this.keySet(connection,tr);
            foreach (int i in k)
            {
                b.Add(this.get(i, connection,tr));
            }
            return b;
        }

        //TODO daqui para baixo

        private Atividade put(Atividade novo, SqlConnection connection,SqlTransaction tr)
        {
            Atividade b = this.get(novo.idAtividade, connection,tr);
            String queryString=null;
            BatedorDAO bd = new BatedorDAO(this.dbConf);
            
            if (b == null) //inserie
            {
                queryString = String.Format("INSERT INTO dbo.Atividade " +
                    "(id_Atividade, InicioReconhecimento, FimReconhecimento, InProgress,Equipa_Email,Equipa_Nome,Batedor,Done) " +
                    " VALUES " +
                    " ({0}, '{1}', '{2}', {3},'{4}','{5}','{6}',{7}); ",
                          novo.idAtividade, novo.inicioReconhecimento.ToString("yyyy-MM-dd HH':'mm':'ss"), 
                          novo.fimReconhecimento.ToString("yyyy-MM-dd HH':'mm':'ss"), Convert.ToInt32(novo.inprogress),
                          novo.equipa.email,novo.equipa.nome,
                          novo.batedor.email, Convert.ToInt32(novo.done));

            }
            else//update
            {
                bd.put(novo.batedor, connection, tr);
                queryString = String.Format("UPDATE dbo.Atividade " +
                    " SET InicioReconhecimento = '{0}' , FimReconhecimento= '{1}' , InProgress= {2} ,Equipa_Email= '{3}' ,Equipa_Nome= '{4}' ,Batedor= '{5}' ,Done= {6}" +
                    " WHERE id_Atividade= {7} ;",
                          novo.inicioReconhecimento.ToString("yyyy-MM-dd HH':'mm':'ss"), novo.fimReconhecimento.ToString("yyyy-MM-dd HH':'mm':'ss"), Convert.ToInt32(novo.inprogress), novo.equipa.email,novo.equipa.nome,novo.batedor.email, Convert.ToInt32(novo.done),novo.idAtividade);

            }
            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            command.ExecuteNonQuery();

            VeiculosDAO vd = new VeiculosDAO(novo.idAtividade);
            foreach(Veiculo v in novo.veiculos)
            {
                vd.put(v,connection,tr);
            }
            
            MapaDAO md = new MapaDAO();
            md.put(novo.percurso, connection,tr);
            NotaDAO nd = new NotaDAO(novo.idAtividade);
            foreach (Nota n in novo.notas)
            {
                nd.put(n, connection,tr);
            }

           
            return b;
        }


        private Atividade get(int id, SqlConnection connection,SqlTransaction tr)
        {

            Atividade a = null;


            VeiculosDAO vd = new VeiculosDAO(id);
            MapaDAO md = new MapaDAO();
            NotaDAO nd = new NotaDAO(id);
            BatedorDAO bd = new BatedorDAO(this.dbConf);
            
           
            List<Veiculo> veiculos = vd.Values(connection,tr);
            Mapa mapa = md.get(id,connection,tr);
            List<Nota> notas = nd.Values(connection,tr);
            Batedor bate = null;

            //buscar merdas a tabela de atvidades
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT * from dbo.Atividade " +
                    "WHERE id_Atividade = {0};",id);

            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            if (reader.Read())
            {
                DateTime inicioR = Convert.ToDateTime(reader["InicioReconhecimento"]);
                DateTime fimR = Convert.ToDateTime(reader["FimReconhecimento"]);
                bool inProg = (bool)reader["InProgress"];
                var v =  Convert.ToBoolean(reader["Done"]);
                //MessageBox.Show(v.GetType().ToString());
                bool done = (bool)v;
                string mailEquipa = reader["Equipa_Email"] as string;
                string nomeEquipa = reader["Equipa_Nome"] as string;
                string mailbate = reader["Batedor"] as string;
                bate = bd.get(mailbate, connection,tr);
                a = new Atividade(id, inicioR, fimR, nomeEquipa, inProg, done, notas, mapa, veiculos, new Equipa(nomeEquipa, mailEquipa), bate);
            }
            reader.Close();
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

        internal Veiculo get(string chassi, SqlConnection connection, SqlTransaction tr)
        {
            Veiculo b = null;
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT * FROM dbo.Veiculo " +
                    "WHERE Chassi = '{0}' AND Atividade = {1};",
                          chassi,this.idatividade);

            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var chass = reader[0];
                var marca = reader[1];
                var modelo = reader[2];
                int ativ = this.idatividade;

                List<string> caract = new List<string>();
                string queryStringCarac = String.Format("SELECT * FROM dbo.VeiculoCaracteristicas " +
                    "WHERE Chassi = '{0}';",
                          chass);

                SqlCommand commandVC = new SqlCommand(queryStringCarac, connection,tr);
                commandVC.CommandTimeout = 60;
                SqlDataReader readerVC = commandVC.ExecuteReader();
                while (readerVC.Read())
                {
                    caract.Add(readerVC[0] as string);
                }
                readerVC.Close();

                b = new Veiculo(modelo as string, marca as string, chass as string, caract);
            }
            reader.Close();
            return b;
        }

        internal List<String> keySet(SqlConnection connection,SqlTransaction tr)
        {
            List<String> r = new List<string>();
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT Chassi from dbo.Veiculo WHERE Atividade = {0} ;",
                          this.idatividade);
            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            //command.Transaction = connection.
            //MessageBox.Show(connection.State.ToString());
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var email = reader[0];


                r.Add(email as string);

            }
            reader.Close();
            return r;
        }

        internal List<Veiculo> Values(SqlConnection connection,SqlTransaction tr)
        {
            List<Veiculo> b = new List<Veiculo>();
            List<String> k = this.keySet(connection,tr);
            foreach (string s in k)
            {
                b.Add(this.get(s, connection,tr));
            }
            return b;
        }


        internal Veiculo put(Veiculo novo, SqlConnection connection,SqlTransaction tr)
        {
            Veiculo v = this.get(novo.chassi, connection,tr);
            String queryString;
            if (v == null) //inserie
            {
                queryString = String.Format("INSERT INTO dbo.Veiculo " +
                    "(Chassi, Marca, Modelo, Atividade) " +
                    " VALUES " +
                    " ('{0}', '{1}', '{2}', {3}); ",
                          novo.chassi,novo.marca,novo.modelo,this.idatividade);

                SqlCommand command = new SqlCommand(queryString, connection,tr);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                List<String> cara = novo.caracteristicas;
                String queryStringC;
                foreach(string c in cara)
                {
                    queryStringC = String.Format("INSERT INTO dbo.VeiculoCaracteristicas " +
                    "(Caracteristica, Chassi, Atividade) " +
                    " VALUES " +
                    " ('{0}', '{1}' , {2}); ",
                          c,novo.chassi, this.idatividade);
                    command = new SqlCommand(queryStringC, connection,tr);
                    command.CommandTimeout = 60;
                    command.ExecuteNonQuery();
                }
            }
            else//update
            {
                // UPDATE dbo.Batedor
                // SET Nome = 'ze', Password = 'novo', HorasDeReconhecimento = 10, N_Atividades = 10
                // WHERE Email = 'a@a.pt';
                queryString = String.Format("UPDATE dbo.Veiculo " +
                    " SET Marca = '{0}', Modelo = '{1}' " +
                    " WHERE Chassi = '{2}'  AND Atividade = {3} ;",
                          novo.marca, novo.modelo, novo.chassi, this.idatividade);
                SqlCommand command = new SqlCommand(queryString, connection,tr);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                //esta update aos veiculos
                List<String> cara = novo.caracteristicas;
                String queryStringC;
                foreach (string c in cara)
                {

                    string queryStringCarac = String.Format("SELECT * from dbo.VeiculoCaracteristicas " +
                    "WHERE Chassi = '{0}' AND Caracteristica = '{1}';",
                          novo.chassi, c);

                    SqlCommand commandVC = new SqlCommand(queryStringCarac, connection,tr);
                    commandVC.CommandTimeout = 60;
                    SqlDataReader readerVC = command.ExecuteReader();
                    if (!readerVC.Read())
                    {//novo
                        queryStringC = String.Format("INSERT into dbo.VeiculoCaracteristicas " +
                            "(Caracteristica, Chassi) " +
                            " VALUES " +
                            " ('{0}', '{1}'); ",
                          c, novo.chassi);
                        command = new SqlCommand(queryString, connection,tr);
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
        internal Mapa get(int id, SqlConnection connection, SqlTransaction tr)
        {
            Mapa m = null;
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT * from dbo.Mapa " +
                    "WHERE id_Mapa = {0} ;",id);

            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            if (reader.Read())
            {
                var NomeProva = reader[1];


                Dictionary<int,GeoCoordinate> cords = new Dictionary<int, GeoCoordinate>();
                string queryStringCarac = String.Format("SELECT * from dbo.Coordenadasmapa " +
                    "WHERE Mapa = '{0}';",
                          id);
                reader.Close();
                SqlCommand commandVC = new SqlCommand(queryStringCarac, connection,tr);
                commandVC.CommandTimeout = 60;
                SqlDataReader readerC = commandVC.ExecuteReader();
                while (readerC.Read())
                {
                    var idC = readerC[0];
                    var longit = readerC[1];
                    var lat = readerC[2];

                    //MessageBox.Show("lat" + Double.Parse(lat.ToString().Replace(',','.'), CultureInfo.InvariantCulture) + "long" + Double.Parse(longit.ToString().Replace(',', '.'), CultureInfo.InvariantCulture));
                    GeoCoordinate gnew = new GeoCoordinate(Double.Parse(lat.ToString().Replace(',', '.'), CultureInfo.InvariantCulture), Double.Parse(longit.ToString().Replace(',', '.'), CultureInfo.InvariantCulture));
                    cords.Add(Int32.Parse(idC.ToString()), gnew);
                }
                readerC.Close();

                m = new Mapa(NomeProva as string, id, cords);
            }
            if (!reader.IsClosed)
            {
                reader.Close();

            }
            return m;
        }

        internal List<int> keySet(SqlConnection connection, SqlTransaction tr)
        {
            List<int> r = new List<int>();
            DataTable results = new DataTable();

            string queryString = "SELECT id_Mapa from dbo.Mapa;";
            
            SqlCommand command = new SqlCommand(queryString, connection,tr);
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

        internal List<Mapa> Values(SqlConnection connection,SqlTransaction tr)
        {
            List<Mapa> b = new List<Mapa>();
            List<int> k = this.keySet(connection,tr);
            foreach (int s in k)
            {
                b.Add(this.get(s, connection,tr));
            }
            return b;
        }


        internal Mapa put(Mapa novo, SqlConnection connection,SqlTransaction tr)
        {
            Mapa m = this.get(novo.idMapa, connection,tr);
            String queryString;
            if (m == null) //inserie
            {
                queryString = String.Format("INSERT INTO dbo.Mapa " +
                    "(id_Mapa, NomeProva) " +
                    " VALUES " +
                    " ({0}, '{1}'); ",
                          novo.idMapa, novo.nomeProva);

                SqlCommand command = new SqlCommand(queryString, connection,tr);
                command.CommandTimeout = 60;
                command.ExecuteNonQuery();
                Dictionary<int,GeoCoordinate> cords = novo.cords;
                String queryStringC;
                foreach (int key in cords.Keys)
                {
                    queryStringC = String.Format("INSERT INTO dbo.Coordenadasmapa " +
                    "(NrCoordenada, Longitude,Latitude,Mapa) " +
                    " VALUES " +
                    " ({0}, {1},{2}, {3}); ",
                          key,cords[key].Longitude.ToString().Replace(',','.'),cords[key].Latitude.ToString().Replace(',', '.'), novo.idMapa);
                    command = new SqlCommand(queryStringC, connection,tr);
                    command.CommandTimeout = 60;
                    command.ExecuteNonQuery();
                }
            }
            /*else//update
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
            }*/
            return m;
        }

    }


    internal class NotaDAO
    {
        public int idAtividade { get; set; }
        
        public NotaDAO(int atividade)
        {
            this.idAtividade = atividade;
        }

        internal Nota get(int id, SqlConnection connection,SqlTransaction tr)
        {
            Nota n = null;
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT * from dbo.Nota " +
                    "WHERE id_Nota = '{0}' AND Atividade = {1};",
                          id, this.idAtividade);

            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            if (reader.Read())
            {
                string notaText = reader["NotaTextual"] as string;
                var au = reader["Audio"];
                byte[] audio = null;
                if (reader.IsDBNull(3)==false)
                {
                    audio = (byte[])reader["Audio"];
                }
                
                string textoConvert = reader["TextoConvertido"] as string;
                double longitude = (double)reader["Longitude"];  
                double latitude = (double)reader["Latitude"];

                GeoCoordinate local = new GeoCoordinate(latitude, longitude);

                //ir buscar as imagens
                List<Image> images = new List<Image>();
                Dictionary<int, Image> d = new Dictionary<int, Image>();
                string queryStringImagem = String.Format("SELECT * from dbo.Imagem " +
                    "WHERE Atividade = '{0}' AND Nota = {1};",
                          this.idAtividade,id);

                SqlCommand commandIM = new SqlCommand(queryStringImagem, connection,tr);
                commandIM.CommandTimeout = 60;
                SqlDataReader readerIM = commandIM.ExecuteReader();
                //vai buscar cada imagem
                while (readerIM.Read())
                {
                    var v = readerIM["id_Image"];
                    byte[] imagem  = (byte[])readerIM["Image"];
                    //int num = reader.GetInt32(readerIM.GetOrdinal("id_Image"));
                    int num = (int)v;
                    string s = BackOfficeAPP.fromBytes64(imagem);
                    d.Add(num,BackOfficeAPP.imageFromBitMapRep(s));
                }
                readerIM.Close();
                //meter as imagens por ordem
                foreach(int i in d.Keys)
                {
                    images.Add(d[i]);
                }
                n = new Nota(id, notaText, local, images, audio,new Voz(audio, textoConvert));
               // b = new Veiculo(modelo as string, marca as string, chass as string, caract);
            }
            reader.Close();
            return n;
        }

        internal List<int> keySet(SqlConnection connection,SqlTransaction tr)
        {
            List<int> r = new List<int>();
            DataTable results = new DataTable();

            string queryString = String.Format("SELECT id_Nota from dbo.Nota WHERE Atividade = {0} ;",
                          this.idAtividade);
            SqlCommand command = new SqlCommand(queryString, connection,tr);
            command.CommandTimeout = 60;
            SqlDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                var id_Nota = reader[0];


                r.Add(Int32.Parse(id_Nota.ToString()));

            }
            reader.Close();
            return r;
        }

        internal List<Nota> Values(SqlConnection connection,SqlTransaction tr)
        {
            List<Nota> b = new List<Nota>();
            List<int> k = this.keySet(connection,tr);
            foreach (int i in k)
            {
                b.Add(this.get(i, connection,tr));
            }
            return b;
        }

        private byte[] imageToByteArray(Image imageIn)
        {
            using (var ms = new MemoryStream())
            {
                imageIn.Save(ms, System.Drawing.Imaging.ImageFormat.Gif);
                return ms.ToArray();
            }
        }

        internal Nota put(Nota novo, SqlConnection connection,SqlTransaction tr)
        {
            Nota n = this.get(novo.idNota, connection,tr);
            String queryString;
            if (n == null) //inserie
            {
                if (novo.asVoice())
                {
                    /*queryString = String.Format("INSERT INTO dbo.Nota " +
                    "(id_Nota, NotaTextual, Audio, TextoConvertido,Latitude,Longitude,Atividade) " +
                    " VALUES " +
                    " ({0}, '{1}', {2}, '{3}', {4}, {5}, {6}); ",
                          novo.idNota, novo.notaTextual, novo.notasVoz.audio, novo.notasVoz.texto,
                          novo.localRegisto.Latitude.ToString().Replace(',', '.'), novo.localRegisto.Longitude.ToString().Replace(',', '.'),
                          this.idAtividade);*/

                    queryString = "INSERT INTO dbo.Nota " +
                   "(id_Nota, NotaTextual, Audio, TextoConvertido,Latitude,Longitude,Atividade) VALUES (@idNota, @notaTextual , @audio , @textoConvertido, @Lat,@Lon,@ativ); ";

                    SqlCommand command = new SqlCommand(queryString, connection, tr);
                    command.Parameters.AddWithValue("idNota", novo.idNota);
                    command.Parameters.AddWithValue("notaTextual", novo.notaTextual);
                    command.Parameters.AddWithValue("audio", novo.notasVoz.audio);
                    command.Parameters.AddWithValue("textoConvertido", novo.notasVoz.texto);
                    command.Parameters.AddWithValue("Lat", novo.localRegisto.Latitude.ToString().Replace(',', '.'));
                    command.Parameters.AddWithValue("Lon", novo.localRegisto.Longitude.ToString().Replace(',', '.'));
                    command.Parameters.AddWithValue("ativ", this.idAtividade);
                    command.CommandTimeout = 60;
                    command.ExecuteNonQuery();
                }
                else
                {
                    queryString = String.Format("INSERT INTO dbo.Nota " +
                    "(id_Nota, NotaTextual, Audio, TextoConvertido,Latitude,Longitude,Atividade) " +
                    " VALUES " +
                    " ({0}, '{1}', null, null, {2}, {3}, {4}); ",
                          novo.idNota, novo.notaTextual,
                          novo.localRegisto.Latitude.ToString().Replace(',', '.'), novo.localRegisto.Longitude.ToString().Replace(',', '.'),
                          this.idAtividade);

                    SqlCommand command = new SqlCommand(queryString, connection,tr);
                    command.CommandTimeout = 60;
                    command.ExecuteNonQuery();
                }
                

                List<Image> images = novo.imagens;
                String queryStringC;
                int i = 0;
                if (images != null)
                {
                    foreach (Bitmap b in images)
                    {
                        Image c = (Image)b;
                        String s = BackOfficeAPP.bitMapRepStringFromImaga(c);
                        byte[] array = BackOfficeAPP.toBytes64(s);
                        queryStringC = "INSERT INTO dbo.Imagem " +
                        "(Image, Nota,Atividade,id_Image) VALUES (@img, @idNota , @idAtividade , @i); ";

                        String x = BackOfficeAPP.fromBytes64(array);
                        SqlCommand command = new SqlCommand(queryStringC, connection, tr);
                        command.Parameters.AddWithValue("img", array);
                        command.Parameters.AddWithValue("idNota", novo.idNota);
                        command.Parameters.AddWithValue("idAtividade", this.idAtividade);
                        command.Parameters.AddWithValue("i", i);
                        //System.Text.Encoding.UTF8.GetString(array)
                        command.CommandTimeout = 60;
                        //MessageBox.Show(System.Text.Encoding.UTF8.GetString(array));
                        command.ExecuteNonQuery();
                        i++;
                    }
                }
            }/*
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
            }*/
            return n;
        }
    }
}
