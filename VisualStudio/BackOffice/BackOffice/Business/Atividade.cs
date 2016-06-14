using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BackOffice;
using BackOffice.Business.Exceptions;
using itextsharp.pdfa;
using itextsharp;
using iTextSharp;
using iTextSharp.text;
using iTextSharp.awt;
using iTextSharp.testutils;
using iTextSharp.xmp;
using iTextSharp.xtra;
using iTextSharp.text.pdf;
using System.IO;

namespace BackOffice.Business
{
    public class Atividade
    {
        public int idAtividade { get; set; }
        public DateTime inicioReconhecimento { get; set; }
        public DateTime fimReconhecimento { get; set; }
        public string nomeEquipa { get; set; }
        public Boolean inprogress { get; set; }
        public Boolean done { get; set; }
        ///
        public List<Nota> notas { get; set; }
        public Mapa percurso { get; set; }
        public List<Veiculo> veiculos { get; set; }
        public Equipa equipa { get; set; }
        public Batedor batedor { get; set; }

        public Atividade(int id) //so para a leitura a partir do json
        {
            this.idAtividade = id;
            this.inicioReconhecimento = new DateTime(); //nao deixa meter nulo
            this.fimReconhecimento = new DateTime(); //nao deixa meter nulo
            this.nomeEquipa = null;
            this.inprogress = false;
            this.done = false;
            this.notas = new List<Nota>();
            this.percurso = null;
            this.veiculos = null;
            this.equipa = null;
            this.batedor = null;
        }

        public Atividade(int id, string mailEquipa, string nomeProva
            , string mapPath, List<Veiculo> veicls, Equipa equip,
            Batedor bat)
        {
            this.idAtividade = id;
            this.inicioReconhecimento = new DateTime(); //nao deixa meter nulo
            this.fimReconhecimento= new DateTime(); //nao deixa meter nulo
            this.nomeEquipa = equip.nome;
            this.inprogress = false;
            this.done = false;
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

        public void stopReconhecimento()
        {
            if (this.inprogress == false)
            {
                throw new AtividadeNaoIniciadaException("Atividade Não Foi Iniciada");
            }
            this.inprogress = false;
            this.done = true;
            this.fimReconhecimento = DateTime.Now;
        }

        public void addVeiculo(string mod, string marc, string chassie, List<string> carac)
        {
            Veiculo v = new Veiculo(mod, marc, chassie, carac);
            this.addVeiculo(v);
        }

        public void generateReportCopiloto(string path)
        {

            var NotaFont = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            var TitleFont = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            var distFont = FontFactory.GetFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Document doc = new Document(iTextSharp.text.PageSize.LETTER, 10, 10, 42, 35);
            PdfWriter wri = PdfWriter.GetInstance(doc, new FileStream(path, FileMode.Create));
            doc.Open();
            Paragraph p = new Paragraph("Reprot from " + percurso.nomeProva + " to Team " + equipa.nome + ".\n\n\n\n", TitleFont);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);

            PdfPTable t = new PdfPTable(2);
            foreach(Nota n in notas){
                if (n.asVoice())
                {
                    
                    String nota = n.getToPiloto();
                    double startDist = n.getDistanceToBegin(this.percurso);
                    double endDist = n.getDistanceToFinish(this.percurso);
                    String dist = startDist + "\n\n("+ endDist+")";
                    var c1 = new PdfPCell(new Phrase(dist, distFont));
                    var c2 = new PdfPCell(new Phrase(nota, NotaFont));
                    c1.HorizontalAlignment = Element.ALIGN_CENTER;
                    c1.VerticalAlignment = Element.ALIGN_CENTER;
                    c2.HorizontalAlignment = Element.ALIGN_CENTER;
                    c2.VerticalAlignment = Element.ALIGN_CENTER;
                    t.AddCell(c1);
                    t.AddCell(c2);
                }
            }

            doc.Add(t);
            doc.Close();
        }

    }




}
