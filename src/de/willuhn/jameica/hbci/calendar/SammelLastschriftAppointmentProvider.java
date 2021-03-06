/**********************************************************************
 * $Source: /cvsroot/hibiscus/hibiscus/src/de/willuhn/jameica/hbci/calendar/SammelLastschriftAppointmentProvider.java,v $
 * $Revision: 1.10 $
 * $Date: 2012/02/20 17:03:50 $
 * $Author: willuhn $
 *
 * Copyright (c) by willuhn - software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.calendar;

import java.rmi.RemoteException;

import org.eclipse.swt.graphics.RGB;

import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.hbci.rmi.SammelLastschrift;
import de.willuhn.jameica.hbci.schedule.Schedule;
import de.willuhn.logging.Logger;

/**
 * Implementierung eines Termin-Providers fuer offene Sammel-Lastschriften.
 */
public class SammelLastschriftAppointmentProvider extends AbstractAppointmentProvider<SammelLastschrift>
{
  
  /**
   * @see de.willuhn.jameica.hbci.calendar.AbstractTransferAppointmentProvider#createAppointment(de.willuhn.jameica.hbci.rmi.Terminable, java.util.Date)
   */
  AbstractHibiscusAppointment createAppointment(Schedule<SammelLastschrift> schedule)
  {
    return new MyAppointment(schedule);
  }

  /**
   * Hilfsklasse zum Anzeigen und Oeffnen des Appointments.
   */
  private class MyAppointment extends AbstractHibiscusAppointment
  {
    /**
     * ct.
     * @param schedule der Auftrag.
     */
    private MyAppointment(Schedule<SammelLastschrift> schedule)
    {
      super(schedule);
    }

    /**
     * @see de.willuhn.jameica.hbci.calendar.AbstractAppointmentProvider.AbstractHibiscusAppointment#getColor()
     */
    public RGB getColor()
    {
      // Ueberschrieben, um Lastschriften in gruen anzuzeigen
      if (this.schedule.isPlanned() || !this.hasAlarm())
        return Color.COMMENT.getSWTColor().getRGB();
      
      return Settings.getBuchungHabenForeground().getRGB();
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.AbstractAppointment#getDescription()
     */
    public String getDescription()
    {
      try
      {
        SammelLastschrift t = this.schedule.getContext();
        Konto k = t.getKonto();
        return i18n.tr("{0}Sammellastschrift: {1} {2} einziehen\n\n{3}\n\nKonto: {4}",
                       (this.schedule.isPlanned() ? (i18n.tr("Geplant") + ":\n") : ""),
                       HBCI.DECIMALFORMAT.format(t.getSumme()),
                       k.getWaehrung(),
                       t.getBezeichnung(),
                       k.getLongName());
      }
      catch (RemoteException re)
      {
        Logger.error("unable to build description",re);
        return null;
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getName()
     */
    public String getName()
    {
      try
      {
        SammelLastschrift t = this.schedule.getContext();
        Konto k = t.getKonto();
        return i18n.tr("{0} {1} {2}",HBCI.DECIMALFORMAT.format(t.getSumme()),k.getWaehrung(),t.getBezeichnung());
      }
      catch (RemoteException re)
      {
        Logger.error("unable to build name",re);
        return i18n.tr("Sammellastschrift");
      }
    }
    
    
  }
}



/**********************************************************************
 * $Log: SammelLastschriftAppointmentProvider.java,v $
 * Revision 1.10  2012/02/20 17:03:50  willuhn
 * @N Umstellung auf neues Schedule-Framework, welches generisch geplante und tatsaechliche Termine fuer Auftraege und Umsaetze ermitteln kann und kuenftig auch vom Forecast verwendet wird
 *
 * Revision 1.8  2012/02/05 12:03:43  willuhn
 * @N generische Open-Action in Basis-Klasse
 *
 * Revision 1.7  2011/12/13 23:10:21  willuhn
 * @N BUGZILLA 1162
 *
 * Revision 1.6  2011-10-06 10:49:24  willuhn
 * @N Termin-Provider fuer Umsaetze
 *
 * Revision 1.5  2011-06-08 15:29:09  willuhn
 * @B Falsche Farbe
 *
 * Revision 1.4  2011-01-20 17:12:39  willuhn
 * @C geaendertes Appointment-Interface
 *
 * Revision 1.3  2010-11-22 00:52:53  willuhn
 * @C Appointment-Inner-Class darf auch private sein
 *
 * Revision 1.2  2010-11-21 23:31:26  willuhn
 * @N Auch abgelaufene Termine anzeigen
 * @N Turnus von Dauerauftraegen berechnen
 *
 * Revision 1.1  2010-11-19 18:37:20  willuhn
 * @N Erste Version der Termin-View mit Appointment-Providern
 *
 **********************************************************************/