using System;
using System.Collections.Generic;
using System.Speech.Recognition;
using System.IO;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using BackOffice.Business.Exceptions;
using System.Net.Mail;
using BackOffice;
using BackOffice.Data;
using BackOffice.Data.Json;
using System.Windows;
using System.Text;
using System.Drawing;
using System.Net.Sockets;
using System.Net;
using BackOffice.Data.DataBase;

namespace BackOffice.Business
{
    public class BackOfficeAPP
    {
        public List<String> batedoresOcupados { get; set; }
        public static Grammar gramatica { get; set; }
        public static Dictionary<String,String> simbolos { get; set; }
        public String email { get; set; }
        public String passMail { get; set; }
        public BatedorDAO batedores { get; set; }
        public AtividadeDAO atividades { get; set; }
        

        //isto é novo nao esta no VP
        public int port { get; set; }
        public string IP { get; set; }
        public string database { get; set; }


        //é para apagar 
        public Dictionary<int, Atividade> atividadeFE { get; set; }
        public Dictionary<int, Atividade> atividadeTERM { get; set; }

        public BackOfficeAPP(String confJSON)
        {
            string json;
            using (StreamReader r = new StreamReader(confJSON))
            {
                json  = r.ReadToEnd();
            }
            StartConfig s;
            using (var sr = new StringReader(json))
            using (var jr = new JsonTextReader(sr))
            {
                var js = new JsonSerializer();
                s = js.Deserialize<StartConfig>(jr);

            }
            //Ja tenho as configuraçoes
            try
            {
                this.IP = GetLocalIPAddress();
            }
            catch(Exception e)
            {
                this.IP = "0.0.0.0";
            }
            this.port = Int32.Parse( s.port);
            this.passMail = s.password;
            this.email = s.email;
            this.database = s.database;
            //gramatica
            string[] gram = s.dicionario;
            Choices escolhas = new Choices();
            for (int i = 0; i < gram.Length; i++)
            {
                escolhas.Add(gram[i]);
            }
            GrammarBuilder gb = new GrammarBuilder();
            gb.Append(escolhas);
            BackOfficeAPP.gramatica = new Grammar(gb);
            //dicionario traduçao
            BackOfficeAPP.simbolos = new Dictionary<string, string>();
            String corner = "corner ";
            Corner corn = s.simbolos.corner;
            String grade = "grade ";
            Dictionary<String, String> graded = corn.grade;
            foreach(String key in graded.Keys)
            {
                String valor = corner + grade + graded[key];
                BackOfficeAPP.simbolos.Add(key, valor);
            }

            String duration = "duration ";

            Dictionary<String, String> dur = corn.duration;
            foreach (String key in dur.Keys)
            {
                String valor = corner + duration + dur[key];
                BackOfficeAPP.simbolos.Add(key, valor);
            }

            String further = "further ";

            Dictionary<String, String> fur = corn.further;
            foreach (String key in fur.Keys)
            {
                String valor = corner + further + fur[key];
                BackOfficeAPP.simbolos.Add(key, valor);
            }
            String road = "road ";

            Dictionary<String, String> ro = s.simbolos.road;
            foreach (String key in ro.Keys)
            {
                String valor = road + ro[key];
                BackOfficeAPP.simbolos.Add(key, valor);
            }


            //TODO BD dos 
            this.atividades = new AtividadeDAO(this.database);
            this.batedores = new BatedorDAO(this.database);

            //Isto é para apagar
            this.atividadeFE = new Dictionary<int, Atividade>();
            this.atividadeTERM = new Dictionary<int, Atividade>();
            //
        }


        public void startReceive()
        {

        }

        public void stopReceive()
        {

        }
        private static string GetLocalIPAddress()
        {
            var host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (var ip in host.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    return ip.ToString();
                }
            }
            throw new Exception("Local IP Address Not Found!");
        }


        public List<Batedor> getBatedores()
        {
            return this.batedores.Values();
        }


        public List<String> getBatedoresMails()
        {
            return this.batedores.keySet();
        }

        private Batedor getBatedor(string mail)
        {
            return this.batedores.get(mail);
        }



        private void guardaNovaAtividade(Atividade a)
        {
            //Isto é para apagar
            this.atividadeFE.Add(a.idAtividade, a);
            //
            this.atividades.put(a);
        }

        private int getNextAtividadeID() //pode dar merda
        {
            List<Int32> l = this.atividades.keySet();
            if (l.Count == 0) return 0;
            l.Sort();
            return l[l.Count - 1]+1;
        }

        public void registarAtividade(string mailBatedor, string mapPath, string nomeprova,
            string nomeEquipa, string mailEquipa, List<Veiculo> lv)
        {
            int idAtividade = this.getNextAtividadeID();
            this.registarAtividade(idAtividade, mailBatedor, mapPath, nomeprova,
             nomeEquipa, mailEquipa, lv);

        }

        private void registarAtividade(int idAtividade, string mailBatedor,string mapPath, string nomeprova,
            string nomeEquipa, string mailEquipa, List<Veiculo> lv)
        {
            Batedor b = this.getBatedor(mailBatedor);
            Atividade a = new Atividade(idAtividade, mailEquipa, nomeprova, mapPath, lv, new Equipa(nomeEquipa, mailEquipa), b);
            this.guardaNovaAtividade(a);
            //depois apagar
            String s = this.jsonFrom(idAtividade);
            System.IO.File.WriteAllText("C:\\Users\\Octávio\\Desktop\\novo.json", s);
        }



        private Atividade getAtividade(int id)
        {
            return this.atividades.get(id);
        }

        public List<Atividade> getAtividadesTerminadas()
        {
            List<Atividade> l = this.getAtividades();
            List<Atividade> ret = new List<Atividade>();
            foreach(Atividade a in l)
            {
                if (a.done)
                {
                    ret.Add(a);
                }
            }
            return ret;

        }

        public List<Atividade> getAtividadesPorTerminar()
        {
            List<Atividade> l = this.getAtividades();
            List<Atividade> ret = new List<Atividade>();
            foreach (Atividade a in l)
            {
                if (!a.done)
                {
                    ret.Add(a);
                }
            }
            return ret;
        }

        public List<Atividade> getAtividades()
        {
            return this.atividades.Values();
        }

        public List<int> getAtividadesTerminadasID()
        {
            List<Atividade> l = this.getAtividadesTerminadas();
            List<int> ret = new List<int>();
            foreach (Atividade a in l)
            {
                if (a.done)
                {
                    ret.Add(a.idAtividade);
                }
            }
            return ret;
        }

        public List<int> getAtividadesPorTerminarID()
        {
            List<Atividade> l = this.getAtividadesPorTerminar();
            List<int> ret = new List<int>();
            foreach (Atividade a in l)
            {
                if (!a.done)
                {
                    ret.Add(a.idAtividade);
                }
            }
            return ret;
        }

        public List<int> getAtividadesID()
        {
            return this.atividades.keySet();
        }



        public Atividade consultarAtividadeTerm(int id)
        {
            Atividade a = this.getAtividade(id);
            if (a == null)
            {
                throw new NaoExisteAtividadeExeption("Nao Existe atividade " + id);
            }
            if (a.done == false)
            {
                throw new AtividadeNaoTerminadaExeption("A atividade " + id + " nao terminada");
            }
            return a;
        } 
        

        public Boolean existeBatedor(string mail)
        {
            return this.batedores.containsKey(mail);
        }

        public void registarBatedor(Batedor b)
        {
            this.batedores.put(b);
        }


        public Batedor consultarFichaBatedor(string mail)
        {
            Batedor b = this.getBatedor(mail);
            if (b == null)
            {
                throw new NaoExisteBatedorExeption("Batedor com mail " + mail + " Desconehcido");
            }
            return b;
        }

        public void receberAtividade()
        {
            //TODO
        }


        public void enviarAtividade()
        {
            //TODO
        }

        public JustToBack formJson(string json) //atividade parcial atençap 
        {
            JustToBack u;
            using (var sr = new StringReader(json))
            using (var jr = new JsonTextReader(sr))
            {
                var js = new JsonSerializer();
                u= js.Deserialize<JustToBack>(jr);
               
            }
            return u;
        }

        public Atividade formJson(JustToBack u) //atençao que isto retorna um atividade parcial
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
                foreach(String s in no.imagem)
                {
                    byte[] ia = Encoding.ASCII.GetBytes(s);
                    Image i;
                    using (var ms = new MemoryStream(ia)) 
                    {
                        i= Image.FromStream(ms);
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

        private string jsonFrom(int idAtividade) //retorna string do json par enviar 
        {
            Atividade a = this.getAtividade(idAtividade);
            BackToJust jsonClass = new BackToJust(a);
            string json = JsonConvert.SerializeObject(jsonClass, Formatting.Indented);
            return json;
        }


        public void gerarRelatorios(string path, int atividade_id)
        {
            string pathPiloto = path + "copilot.pdf";
            Atividade a = this.getAtividade(atividade_id);
            if (this.getAtividadesPorTerminarID().Contains(atividade_id))
            {
                throw new AtividadeNaoIniciadaException("Atividade " + atividade_id + " Nao Terminada");
            }
            a.generateReportCopiloto(pathPiloto);
            string pathGlobal = path + "general.pdf";

            //TODO este metodo ainda nao faz nada
            a.generateReportGlobal(pathGlobal);
            
        }



        public void email_send(string recipient, string subject, string body, List<string> attachmentFilenames)
        {
            MailMessage mail = new MailMessage();
            SmtpClient SmtpServer = new SmtpClient("smtp.gmail.com");
            mail.From = new MailAddress(this.email);
            mail.To.Add(recipient);
            mail.Subject = subject;
            mail.Body = body;


            foreach (string attachmentFilename in attachmentFilenames)
            {
                if (attachmentFilename != null)
                {
                    System.Net.Mail.Attachment attachment;
                    attachment = new System.Net.Mail.Attachment(attachmentFilename);
                    mail.Attachments.Add(attachment);
                }
            }

            SmtpServer.Port = 587;
            SmtpServer.Credentials = new System.Net.NetworkCredential(this.email, this.passMail);
            SmtpServer.EnableSsl = true;

            SmtpServer.Send(mail);

        }


     
    }
}
