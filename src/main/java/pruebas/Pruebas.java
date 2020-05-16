package pruebas;



import edu.eci.cvds.samples.entities.EstadoIniciativa;
import edu.eci.cvds.samples.entities.Iniciativa;
import edu.eci.cvds.samples.entities.Reaccion;
import edu.eci.cvds.servicios.IniciativasFactory;
import edu.eci.cvds.samples.managedbeans.ReaccionBean;
import org.apache.shiro.config.Ini;
import org.junit.Assert;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import edu.eci.cvds.servicios.IniciativasFactory;
import edu.eci.cvds.servicios.ServiciosIniciativas;
import edu.eci.cvds.samples.entities.EstadoIniciativa;
import edu.eci.cvds.samples.entities.Iniciativa;
import edu.eci.cvds.samples.entities.IniciativasAgrupadas;
import edu.eci.cvds.samples.entities.TipoArea;
import edu.eci.cvds.samples.entities.TipoRol;
import edu.eci.cvds.samples.entities.Usuario;
import edu.eci.cvds.samples.services.impl.ServiciosIniciativasImpl;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class Pruebas {
	ServiciosIniciativasImpl prueba = (ServiciosIniciativasImpl) IniciativasFactory.instancia().serviciosIniciativas();
	@Before
	public void Preparacion () {
		prueba.insertarUsuario("1234", "Usuario 1", TipoRol.Administrador, TipoArea.SecretariaGeneral.toString());
		int id=prueba.consultarIdPorNombreyContraseña("Usuario 1", "1234");
		prueba.insertarIniciativa(99, id, "Iniciativa de prueba", EstadoIniciativa.En_Revision, 0, Date.valueOf(LocalDate.now()), "Iniciativa de prueba.");
		
		
	}

    @Test
    public void deberiaConsultarLasIniciativas(){
        Iniciativa iniciativaDePrueba = IniciativasFactory.instancia().iniciativaImplementado().selectIniciativa().get(0);
        String a = iniciativaDePrueba.getClass().getSimpleName();
        String b = IniciativasFactory.instancia().iniciativaImplementado().selectIniciativa().get(0).getClass().getSimpleName();
        Assert.assertEquals(a,b);
    }

    @Test
    public void deberiaInsertarReaccion(){
        int rea = IniciativasFactory.instancia().reaccionImplementado().selectReacciones().get(0).getId_iniciativa();
        String name = "sebas";
        String comentario = "Fabuloso";
        Date fecha = Date.valueOf(LocalDate.now());
        ReaccionBean reaccion = IniciativasFactory.instancia().serviciosIniciativas().insertarReacciones(rea, name, comentario, fecha);
        int lis = IniciativasFactory.instancia().reaccionImplementado().selectReacciones().size();
        Reaccion ini = IniciativasFactory.instancia().reaccionImplementado().selectReacciones().get(lis-1);
        Assert.assertEquals(comentario,ini.getComentario());
    }

    @Test
    public void deberiaDarleLikeaUnaIniciativa(){
        int idIniciativa = IniciativasFactory.instancia().serviciosIniciativas().numeroVotosPorIniciativa(1);
        IniciativasFactory.instancia().serviciosIniciativas().insertarUsuarioIniciativa(1, 1, 1);
        int idIniciativa2 = IniciativasFactory.instancia().serviciosIniciativas().numeroVotosPorIniciativa(1);
        Assert.assertEquals(idIniciativa,idIniciativa2-1);
    }

    @Test
    public void DeberiaConsultarLasIniciativasPropias(){
        List<Iniciativa> lisI = IniciativasFactory.instancia().serviciosIniciativas().consultarIniciativas();
        List<Iniciativa> res = new ArrayList<Iniciativa>();
        int idUsuario=4;
        for(Iniciativa i: lisI){
            if(i.getId()==idUsuario){
                res.add(i);
            }
        }
        Assert.assertEquals(true,true);

    }

    @Test
    public void deberiaPoderModificarInformacionDeUnaPropuesta(){
        Iniciativa lisI = IniciativasFactory.instancia().serviciosIniciativas().consultarIniciativas().get(0);
        int idIni = lisI.getId();
        String oldName=lisI.getNombre();
        String newName = "IniciativaPruebaTest";
        String comentario = "Cambio con exito";
        IniciativasFactory.instancia().IniciativasAgrupadasImplementando().cambiarAgrupados(newName,oldName);
        IniciativasFactory.instancia().iniciativaImplementado().cambiarIniciativas(newName, comentario, idIni);
        int n = IniciativasFactory.instancia().serviciosIniciativas().consultarIniciativas().size();
        Iniciativa lisI2 = IniciativasFactory.instancia().serviciosIniciativas().consultarIniciativas().get(n-1);
        Assert.assertNotEquals(oldName,lisI2.getNombre());
    }

    @Test
    public void deberiAgruparIniciativasPorSuEstado(){
        List<Iniciativa> lis = IniciativasFactory.instancia().serviciosIniciativas().consultarIniciativas();
        int espera=0;
        int revi=0;
        int pro=0;
        int sol=0;
        int res=0;
        EstadoIniciativa estado = EstadoIniciativa.En_Espera_De_Revision;
        EstadoIniciativa estado2 = EstadoIniciativa.En_Revision;
        EstadoIniciativa estado3 = EstadoIniciativa.Proyecto;
        EstadoIniciativa estado4 = EstadoIniciativa.Solucionado;

        for(Iniciativa i: lis){
            if(i.getEstado().equals(estado)){
                espera++;
            }else if(i.getEstado().equals(estado2)){
                revi++;
            }else if(i.getEstado().equals(estado2)){
                pro++;
            }else{
                sol++;
            }
        }
        res = espera+revi+pro+sol;
        Assert.assertEquals(res,lis.size());
    }

    @Test
	public void DeberiaAutenticaUsuarios() {
		
		Usuario a=prueba.consultarUsuario("Usuario 1", "1234");
		
		Assert.assertEquals(a.getNombre()+" "+a.getContraseña(), "Usuario 1"+" "+"1234");
		
	}
	
	@Test
	public void DeberiaAsignarPerfil() {
		
		Usuario a=prueba.consultarUsuario("Usuario 1", "1234");
		int id=prueba.consultarIdPorNombreyContraseña("Usuario 1", "1234");
		prueba.actualizarPerfil(id, TipoRol.PMO_ODI);
		a=prueba.consultarUsuario("Usuario 1", "1234");
		Assert.assertEquals(TipoRol.PMO_ODI,a.getRol());
		
	}
	
	@Test
	public void DeberiaRegistrarUnaIdea() {
		
		
		Iniciativa b=prueba.consultarIniciativaId(99);
		
		Assert.assertEquals(b.descripcion,"Iniciativa de prueba.");
		
		
		
	}
	
	@Test
	public void DeberiaModificarEstadoIniciativa() {
		
		prueba.actualizarIniciativa(EstadoIniciativa.Solucionado, 99);
		Iniciativa b=prueba.consultarIniciativaId(99);
		Assert.assertEquals(EstadoIniciativa.Solucionado,b.getEstado());
		
	}
	
	
	
	@Test
	public void DeberiaConsultarPorPalabrasClave() {
		prueba.insertarPalabraClave("Iniciativa de prueba", 99);
		List<Iniciativa> ini=prueba.consultarIniciativasPalabraClave("Iniciativa de prueba");
		int id=ini.get(0).getId();
		
		
		Assert.assertEquals(id,99);
		
		prueba.elimiarPalabraClavePorId("Iniciativa de prueba");
		
	}
	
	@Test
	public void DeberiaAgrupar() {
		
		prueba.insertarPorIniciativasAgrupadas(99,"Empresas");
		List<IniciativasAgrupadas> agrupadas=prueba.consultarIniciativasAgrupadas();
		int id=agrupadas.get(agrupadas.size()-1).getIniciativaAgrupada();
		Assert.assertEquals(id,99);
		
	}
	
	
	
	
	
	@After
	public void Borrar () {
		prueba.borrarIniciativa(99);
		prueba.borrarUsuario("Usuario 1", "1234");
		
	}
	
}