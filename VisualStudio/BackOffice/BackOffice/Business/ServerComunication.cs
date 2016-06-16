using BackOffice.Data.DataBase;
using BackOffice.Data.Json;
using Newtonsoft.Json;
using Org.BouncyCastle.Utilities.Net;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;

namespace BackOffice.Business
{
    class ServerComunication
    {
        public int listenigPort { get; set; }
        public String dbConf { get; set; }
        public System.Net.IPAddress listeningIP { get; set; }
        private Boolean on;
        private CancellationTokenSource cancellation { get; set; } 
        public ServerComunication(int port, string db)
        {
            this.dbConf = db;
            this.listenigPort = port;
            this.listeningIP = Dns.Resolve("localhost").AddressList[0];
            this.cancellation = new CancellationTokenSource();
            this.on = false;
        }

        public Thread Start()
        {
            Thread newThread = null;
            this.on = true;
            Console.WriteLine(" Comecar ....");

            try
            {
                    newThread = new Thread(new ThreadStart(Run));
                Console.WriteLine(" OK ....");
            }
            catch(Exception e)
            {
                Console.WriteLine(" KO ....");
                this.on = false;
                newThread = null;
            }
            return newThread;

        }

        public void Stop()
        {
             this.on = false;
            this.cancellation.Cancel();
        }

        private async void Run()
        {
            TcpListener serverSocket = null;
            try
            {
                serverSocket = new TcpListener(this.listenigPort);
                serverSocket.Start();
                Console.WriteLine(" Server Started ....");

                while (this.on == true)
                {
                    Console.WriteLine(" ESpera cliente ");

                    // TcpClient clientSocket = serverSocket.AcceptTcpClient();
                    TcpClient clientSocket = await Task.Run(
                                                   () => serverSocket.AcceptTcpClientAsync(),
                                               this.cancellation.Token);

                    Console.WriteLine(" Novo cliente ");
                    //System.Diagnostics.Debug.WriteLine("Novo cliente");


                    ClinetHandler ch = new ClinetHandler(clientSocket, this.dbConf); //nova thread para cliente
                    ch.Start();
                }
            }
            finally
            {
                if (serverSocket != null)
                {
                    serverSocket.Stop();
                }
                this.on = false;
                Console.WriteLine("FIM");
                //System.Diagnostics.Debug.WriteLine("FIM");
            }




        }
    }

    internal class ClinetHandler
    {
        public TcpClient mySocket { get; set; }
        //Connects fine
        public NetworkStream netstream { get; set; } 
        public StreamWriter writerStream { get; set; }
        public StreamReader readStream { get; set; }
        public String dbConfg { get; set; }

        public ClinetHandler(TcpClient s, String db)
        {
            this.dbConfg = db;
            this.mySocket = s;
            this.netstream = this.mySocket.GetStream();
            this.writerStream = new StreamWriter(this.netstream);
            this.readStream = new StreamReader(this.netstream);
           
        }

        public void Start()
        {
            Thread newThread = new Thread(new ThreadStart(Run));
            newThread.Start();
        }
        private void Run()
        {

            //this.writeData("OLA");
            String jsonCheg = this.readData();
            //Console.WriteLine(" jsonCheg: " + jsonCheg);

            this.writeData("REcebi " + jsonCheg);



            try
            {
                JustToBack content = this.fromString(jsonCheg);
                if (content.password != null)//veio a pass quero uma atividade para ele
                {
                    this.sendAtividade(content);

                }
                else //veio uma atividade completa
                {
                    this.processaBatida(content);
                }
            }
            catch (Exception e)
            {
                BackToJust atOK = new BackToJust(-3);
                this.writeData(this.jsonFrom(atOK));
            }
            


            //fechar as comunicaçoes da tread para terminar
            
            this.writerStream.Close();
            this.readStream.Close();
            this.netstream.Close();
            this.mySocket.Close();
        }


        private void processaBatida(JustToBack content)
        {
            Atividade nova = this.formJson(content);
            AtividadeDAO atDAO = new AtividadeDAO(this.dbConfg);
            Atividade old = atDAO.get(nova.idAtividade);
            if (old == null) //nao existe na BD atividade com aqule ID ou seja nao foi criada
            {
                BackToJust atNOK = new BackToJust(-3);
                this.writeData(this.jsonFrom(atNOK));
                return;
            }
            old.stopReconhecimento(nova);
            atDAO.put(old);
            BackToJust atOK = new BackToJust(-2);
            this.writeData(this.jsonFrom(atOK));

        }


        private Atividade formJson(JustToBack u) //atençao que isto retorna um atividade parcial
        {
            Atividade a = new Atividade(u.idAtividade);
            foreach (Note no in u.notas)
            {
                byte[] voice = Encoding.ASCII.GetBytes(no.audio);
                if (voice.Length == 0)
                {
                    voice = null;
                }
                List<Image> li = new List<Image>();
                foreach (String s in no.imagem)
                {
                    byte[] ia = Encoding.ASCII.GetBytes(s);
                    Image i;
                    using (var ms = new MemoryStream(ia))
                    {
                        i = Image.FromStream(ms);
                    }
                    li.Add(i);

                }
                if (li.Count == 0)
                {
                    li = null;
                }
                Nota n = new Nota(no.idNota, no.notaTextual, no.local.lat,
                    no.local.log, li, voice);
            }
            return a;

        }


        private void sendAtividade(JustToBack content)
        {
            BatedorDAO batDao = new BatedorDAO(this.dbConfg);
            List<Batedor> lb = batDao.Values();
            foreach(Batedor b in lb)
            {
                if(b.email.Equals(content.email) && b.password.Equals(content.password)) //passwordOK
                {
                    this.sendAtividadeOK(content.email);
                    return;
                }
                if (b.email.Equals(content.email) && !b.password.Equals(content.password)) //passwordMAL
                {
                    BackToJust wrongPass = new BackToJust(-4);
                    this.writeData(this.jsonFrom(wrongPass));
                    return;

                }
            }

            //Batedor desconhecido
            BackToJust descUser = new BackToJust(-5);
            this.writeData(this.jsonFrom(descUser));

        }

        private void sendAtividadeOK(string toBatodorMail)
        {
            AtividadeDAO atDAO = new AtividadeDAO(this.dbConfg);
            List<Atividade> lats = atDAO.Values();
            ArrayList paraBatedor = new ArrayList();
            foreach(Atividade a in lats)
            {
                if (a.batedor.email.Equals(toBatodorMail))
                {
                    paraBatedor.Add(a);
                }
            }
            if (paraBatedor.Count == 0)
            {
                BackToJust noActiv = new BackToJust(-1);
                this.writeData(this.jsonFrom(noActiv));
                return;
            }
            paraBatedor.Sort();
            Atividade escolhidaA = paraBatedor[0] as Atividade; //proxima atividade para aquele batedor
            this.writeData(this.jsonFrom(escolhidaA));
            //enviada, começar reconhecimento
            escolhidaA.startReconhecimento();
            //guardas na BD
            atDAO.put(escolhidaA);

        }


        private string jsonFrom(Atividade a) //retorna string do json par enviar 
        {
            BackToJust jsonClass = new BackToJust(a);
            string json = JsonConvert.SerializeObject(jsonClass, Formatting.Indented);
            return json;
        }


        private string jsonFrom(BackToJust jsonClass) //retorna string do json par enviar 
        {
            string json = JsonConvert.SerializeObject(jsonClass, Formatting.Indented);
            return json;
        }


        private JustToBack fromString(string json)
        {
            JustToBack s;
            using (var sr = new StringReader(json))
            using (var jr = new JsonTextReader(sr))
            {
                var js = new JsonSerializer();
                s = js.Deserialize<JustToBack>(jr);

            }
            return s;
        }

        private string readData()
        {
            // return this.readStream.ReadToEnd()
            return this.readStream.ReadLine();
        }

        private void writeData(string data)
        {
            string t = Regex.Replace(data, @"\t|\n|\r", "");
            this.writerStream.Write(t);
            this.writerStream.WriteLine();
            this.writerStream.Flush();
        }

    }
}
