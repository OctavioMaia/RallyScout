﻿using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BackOffice.Business;

namespace BackOffice.Data.Json
{
    public class BackToJust
    {
        public int idAtividade { get; set; }
        public String email { get; set; }
        public String nomeEquipa { get; set; }
        public Map mapa { get; set; }
        public Car[] veiculos { get; set; }

        public BackToJust(Atividade a)
        {
            this.idAtividade = a.idAtividade;
            this.email = a.batedor.email;
            this.nomeEquipa = a.nomeEquipa;

            this.mapa = new Map();
            this.mapa.nomeProva = a.percurso.nomeProva;

            int totCords = 0;
            if (a.percurso.cords != null)
            {
                totCords = a.percurso.cords.Count;
            }


            Cord[] cords = new Cord[totCords];
            for (int i = 0; i < totCords; i++)
            {
                GeoCoordinate g = a.percurso.cords[i];
                Cord c = new Cord(g.Latitude, g.Longitude);
                cords[i] = c;
            }
            this.mapa.percurso = cords;

            int totcar = 0;
            if (a.veiculos != null)
            {
                totcar = a.veiculos.Count;
            } 
            Car[] carros = new Car[totcar];
            for(int i = 0; i < totcar; i++)
            {
                Veiculo v = a.veiculos[i];
                string[] caract = new string[v.caracteristicas.Count];
                for(int j=0;j< v.caracteristicas.Count; j++)
                {
                    caract[j] = v.caracteristicas[j];
                }
                Car c = new Car(v.chassi, v.marca,v.modelo,caract);
                carros[i] = c;
            }
            
            this.veiculos = carros;
        }

        public BackToJust(int erro)
        {
            this.idAtividade = erro;
            this.email = null;
            this.nomeEquipa = null;
            this.mapa = null;
            this.veiculos = null;
        }
    }

    public class Map
    {
        public String nomeProva { get; set; }
        public Cord[] percurso { get; set; }
        
    }
    public class Cord
    {
        public double lat { get; set; }
        public double log { get; set; }
        public Cord(double lati,double longi)
        {
            this.lat = lati;
            this.log = longi;
        }
    }
    public class Car
    {
        public string chassi { get; set; }
        public string marca { get; set; }
        public string modelo { get; set; }
        public string[] caracteristicas { get; set; }
        public Car(string chass ,string marca, string modelo, string[] caract)
        {
            this.modelo = modelo;
            this.marca = marca;
            this.chassi = chass;
            this.caracteristicas = caract;
        }
    }
}
