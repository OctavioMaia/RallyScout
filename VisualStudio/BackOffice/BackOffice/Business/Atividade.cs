using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BackOffice;
using BackOffice.Business.Exceptions;


namespace BackOffice.Business
{
    class Atividade
    {
        public int idAtividade { get; set; }
        public DateTime inicioReconhecimento { get; set; }
        public DateTime fimReconhecimento { get; set; }
        public string nomeEquipa { get; set; }
        public Boolean inprogress { get; set; }
        ///
        public List<Nota> notas { get; set; }
        public Mapa percurso { get; set; }
        public List<Veiculo> veiculos { get; set; }
        public Equipa equipa { get; set; }
        public Batedor batedor { get; set; }

        public Atividade(int id, string mailEquipa, string nomeProva
            , string mapPath, List<Veiculo> veicls, Equipa equip,
            Batedor bat)
        {
            this.idAtividade = id;
            this.inicioReconhecimento = new DateTime(); //nao deixa meter nulo
            this.fimReconhecimento= new DateTime(); //nao deixa meter nulo
            this.nomeEquipa = equip.nome;
            this.inprogress = false;
            this.notas = null; //quando uma atividade é inserida nao tem notas
            this.percurso = new Mapa(nomeProva, id, mapPath);
            this.veiculos = veicls;
            this.equipa = equip;
            this.batedor = bat;


        }
        public void addNota(Nota n)
        {
            if (this.notas == null)
            {
                this.notas = new List<Nota>();
                this.notas.Add(n);
            }
            else
            {
               if(this.notas.Contains(n))
                {
                    throw new NotaRepetidaException("Nota " + n.idNota + " Repetida");
                }
                this.notas.Add(n);
            }
        }

        public void addVeiculo(Veiculo v)
        {
            if (this.veiculos == null)
            {
                this.veiculos = new List<Veiculo>();
                this.veiculos.Add(v);
            }
            else
            {
                if (this.veiculos.Contains(v))
                {
                    throw new VeiculoRepetidoException("Veiculo " + v.chassi + " Já existe");
                }
                this.veiculos.Add(v);
            }
        }

        public void startReconhecimento()
        {
            if (this.inprogress == true)
            {
                throw new AtividadeJaIniciadaException("Atividade Já Foi Iniciada");
            }
            this.inprogress = true;
            this.inicioReconhecimento = DateTime.Now;
        }
    }




}
