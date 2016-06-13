﻿using System;
using System.Windows;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BackOffice;
using System.Speech;
using System.Speech.Recognition;
using System.IO;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace BackOffice.Business
{
    class BackOfficeAPP
    {
        public List<String> batedoresOcupados { get; set; }
        public static Grammar gramatica { get; set; }
        public static Dictionary<String,String> simbolos { get; set; }
        public String email { get; set; }
        public String passMail { get; set; }
        public Dictionary<String, Batedor> batedores { get; set; }
        public Dictionary<int,Atividade> atividadeFE { get; set; }
        public Dictionary<int,Atividade> atividadeTERM { get; set; }

        public BackOfficeAPP(String confJSON)
        {
            using (StreamReader r = new StreamReader(confJSON))
            {
                string json = r.ReadToEnd();
                var results = JsonConvert.DeserializeObject<dynamic>(json);
                String mail = results.email;
                String pass = results.password;

                this.email = mail;
                this.passMail = pass;

                JArray dic = results.dicionario;
                string[] arr = dic.ToObject<string[]>();
                Choices escolhas = new Choices();
                for (int i = 0; i < arr.Length; i++)
                 {
                     string s = arr[i];
                    // MessageBox.Show(s);
                    escolhas.Add(s);
                 }
                GrammarBuilder gb = new GrammarBuilder();
                gb.Append(escolhas);
                BackOfficeAPP.gramatica = new Grammar(gb);

                BackOfficeAPP.simbolos = new Dictionary<string, string>();

                JObject simbolos = results.simbolos;
                JObject corner = results.simbolos.corner;
                string root = "corner";
                JObject grade = results.simbolos.corner.grade;
                string bas = root + " grade";
                foreach (KeyValuePair<String, JToken> item in grade)
                {
                    String key = bas +" " +item.Key;
                    String value = item.Value.ToString();
                    BackOfficeAPP.simbolos.Add(key, value);
                }


                JObject duration = results.simbolos.corner.duration;
                bas = root + " duration";

                foreach (KeyValuePair<String, JToken> item in duration)
                {
                    String key = bas + " " + item.Key;
                    String value = item.Value.ToString();
                    BackOfficeAPP.simbolos.Add(key, value);
                }

                JObject further = results.simbolos.corner.further;
                bas = root + " further";

                foreach (KeyValuePair<String, JToken> item in further)
                {
                    String key = bas + " " + item.Key;
                    String value = item.Value.ToString();
                    BackOfficeAPP.simbolos.Add(key, value);
                }

                JObject road = results.simbolos.road;
                bas = "road";
                foreach (KeyValuePair<String, JToken> item in road)
                {
                    String key = item.Key;
                    String value = item.Value.ToString();
                    BackOfficeAPP.simbolos.Add(key, value);
                }   
            }
            //esta tudo lido que vem do json falta o resto
        }

        public void registarAtividade()
        {
            //TODO
        }
        public Atividade consultarAtividade(int id)
        {
            return null;
        } 
        
        public FichaBatedor consultarFichaBatedor(string mail)
        {
            return null;
        }

        public void receberAtividade()
        {

        }
        public void enviarAtividade()
        {

        }


        public void gerarRelatorios(string path, int atividade_id)
        {

        }
        public void enviarEmail(string mail)
        {

        }
    }
}