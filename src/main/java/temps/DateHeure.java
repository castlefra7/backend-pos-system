package temps;

import java.util.GregorianCalendar;
import java.util.Calendar;

public final class DateHeure {

    int jour;
    int mois;
    int annee;

    int heure;
    int minute;
    int seconde;

    public DateHeure() {
        GregorianCalendar newG = new GregorianCalendar();
        setJour(newG.get(newG.DAY_OF_MONTH));
        setMois(newG.get(newG.MONTH) + 1);
        setAnnee(newG.get(newG.YEAR));
        setHeure(newG.get(newG.HOUR_OF_DAY) - 1);
        setMinute(newG.get(newG.MINUTE));
        setSeconde(0);
    }

    public DateHeure(int duree) throws Exception {
        convert(duree);
    }

    public DateHeure(String fullDt) throws Exception {
        setValeurs(fullDt);
    }

    public DateHeure(String date, String heure) throws Exception {
        setJourMoisAnnee(date);
        setHeureMinuteSeconde(heure);
    }

    public DateHeure(int jour, int mois, int annee, int heure, int minute, int seconde) throws Exception {
        setJour(jour);
        setMois(mois);
        setAnnee(annee);

        setHeure(heure);
        setMinute(minute);
        setSeconde(seconde);
    }

    public void setValeurs(String fullDt) throws Exception {
        if (fullDt.isEmpty()) {
            throw new Exception("Date ne peut pas etre vide");
        }
        if (fullDt.contains("/") || fullDt.contains("-") || fullDt.contains(" ")) {

            String[] dt = fullDt.split(" ");
            if (dt.length == 1) {
                setJourMoisAnnee(dt[0]);
                setHeureMinuteSeconde("00:00:00");
            } else if (dt.length == 2) {
                setJourMoisAnnee(dt[0]);
                setHeureMinuteSeconde(dt[1]);
            }

        } else {
            throw new Exception("Erreur: Verifier le format de la date");
        }

    }

    boolean estNombre(String val) {
        try {
            Double valeur = new Double(val);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public DateHeure copy() throws Exception {
        return new DateHeure(getDate(), null);
    }

    void setHeureMinuteSeconde(String duree) throws Exception {
        if (duree == null) {
            setHeure(0);
            setMinute(0);
            setSeconde(0);
            return;
        }

        if ((duree.contains(":") == false)) {
            throw new Exception("Erreur: format de la duree incorrect (hh:mm:ss)");
        }

        if (duree.split(":").length == 1) {
            throw new Exception("Erreur: format de la duree incorrect (hh:mm:ss)");
        }

        String[] tabDuree = duree.split(":");
        int heure;
        int minute;
        int seconde;

        if (estNombre(tabDuree) == false) {
            throw new Exception("Erreur: La duree doit etre un nombre (hh:mm:ss)");
        }

        if (tabDuree.length == 2) {
            heure = (new Integer(0)).parseInt(tabDuree[0]);
            minute = (new Integer(0)).parseInt(tabDuree[1]);
            seconde = 0;
        } else if (tabDuree.length == 3) {
            heure = (new Integer(0)).parseInt(tabDuree[0]);
            minute = (new Integer(0)).parseInt(tabDuree[1]);
            seconde = (new Integer(0)).parseInt(tabDuree[2]);
        } else {
            heure = 0;
            minute = 0;
            seconde = 0;
        }

        setHeure(heure);
        setMinute(minute);
        setSeconde(seconde);
    }

    public int getMilli() throws Exception {
        return convEnMilli(getTime());
    }

    boolean estNombre(String[] tabStr) {
        for (int iT = 0; iT < tabStr.length; iT++) {
            if (estNombre(tabStr[iT]) == false) {
                return false;
            }
        }
        return true;
    }

    void setJourMoisAnnee(String date) throws Exception {
        if (date.isEmpty()) {
            throw new Exception("Erreur: 1. date ne peut pas etre vide");
        }

        if (date == null) {
            throw new Exception("Erreur: 2. La variable ne doit etre pas NULL");
        }

        /*if(date.contains("/") == false || date.contains("-") == false)
        {
            throw new Exception("Erreur: 3. Format de la date invalide (JJ/MM/AAAA)");
        }*/
        if (date.contains("/")) {
            String[] tabDate = date.split("/");

            if (estNombre(tabDate) == false) {
                throw new Exception("Erreur: 4. La date doit etre un nombre (JJ/MM/AAAA)");
            }

            int jour = (new Integer(0)).parseInt(tabDate[0]);
            int mois = (new Integer(0)).parseInt(tabDate[1]);
            int annee = (new Integer(0)).parseInt(tabDate[2]);

            setJour(jour);
            setMois(mois);
            setAnnee(annee);
        }

        if (date.contains("-")) {
            String[] tabDate = date.split("-");

            if (estNombre(tabDate) == false) {
                throw new Exception("Erreur: 5. La date doit etre un nombre (AAAA-MM-JJ)");
            }

            int jour = (new Integer(0)).parseInt(tabDate[2]);
            int mois = (new Integer(0)).parseInt(tabDate[1]);
            int annee = (new Integer(0)).parseInt(tabDate[0]);

            setJour(jour);
            setMois(mois);
            setAnnee(annee);
        }

    }

    public DateHeure ajouter(DateHeure dt) throws Exception {
        if (dt == null) {
            throw new Exception("Erreur: La date ne doit pas etre NULL");
        }
        int seconde = getSeconde() + dt.getSeconde();
        int minute = getMinute() + dt.getMinute();
        int heure = getHeure() + dt.getHeure();
        int jour = 0;
        int mois = 0;
        int annee = 0;

        int compare = comparerDt(dt);
        if (compare == 1) {
            jour = getJour();
            mois = getMois();
            annee = getAnnee();
        } else {
            jour = dt.getJour();
            mois = dt.getMois();
            annee = dt.getAnnee();
        }

        while (seconde >= 60) {
            seconde = seconde - 60;
            minute++;
        }

        while (minute >= 60) {
            minute = minute - 60;
            heure++;
        }

        while (heure >= 24) {
            heure = heure - 24;
            jour++;
        }

        int endDayMonth = getEndDayOfMonth(mois, annee);
        while (jour > endDayMonth) {
            jour = jour - endDayMonth;
            mois++;
        }

        while (mois > 12) {
            mois = mois - 12;
            annee++;
        }

        return new DateHeure(jour, mois, annee, heure, minute, seconde);
    }

    int getEndDayOfMonth(int mois, int annee) {
        GregorianCalendar gcal = new GregorianCalendar();
        switch (mois) {
            case 1:
                return 31;
            case 2:
                if (gcal.isLeapYear(annee)) {
                    return 29;
                } else {
                    return 28;
                }
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
            default:
                return -1;
        }
    }

    public String getDate() {
        String str = new String();
        String jourStr = new String();
        String moisStr = new String();
        if (getJour() <= 9) {
            jourStr = "0".concat(str.valueOf(getJour()));
        } else {
            jourStr = str.valueOf(getJour());
        }

        if (getMois() <= 9) {
            moisStr = "0".concat(str.valueOf(getMois()));
        } else {
            moisStr = str.valueOf(getMois());
        }

        return jourStr.concat("/").concat(moisStr).concat("/").concat(str.valueOf(getAnnee()));
    }

    public String getDateEn() {
        String[] dt = getDate().split("/");
        return dt[2].concat("-").concat(dt[1] + "-").concat(dt[0]);
    }

    public String getTime() {
        String heureStr = new String();
        String minStr = new String();
        String secStr = new String();

        if (getHeure() <= 9) {
            heureStr = "0".concat(heureStr.valueOf(getHeure()));
        } else {
            heureStr = heureStr.valueOf(getHeure());
        }

        if (getMinute() <= 9) {
            minStr = "0".concat(minStr.valueOf(getMinute()));
        } else {
            minStr = minStr.valueOf(getMinute());
        }

        if (getSeconde() <= 9) {
            secStr = "0".concat(minStr.valueOf(getSeconde()));
        } else {
            secStr = secStr.valueOf(getSeconde());
        }
        if (getHeure() == 0) {
            return minStr.concat(":").concat(secStr);
        }
        return heureStr.concat(":").concat(minStr);
    }

    public void convert(int duree) {
        int heure = 0;
        int minute = 0;
        int seconde = 0;
        int enSeconde = duree / 1000;
        while (enSeconde >= 60) {
            enSeconde = enSeconde - 60;
            minute++;
        }
        while (minute >= 60) {
            minute = minute - 60;
            heure++;
        }
        seconde = enSeconde;
        setHeure(heure);
        setMinute(minute);
        setSeconde(seconde);
    }

    public int convEnMilli(String duree) throws Exception {
        if (duree == null) {
            return -1;
        }
        if (!duree.contains(":") || duree.split(":").length == 0) {
            throw new Exception("Format duree incorrecte");
        }
        String[] tabDuree = duree.split(":");
        if (tabDuree.length == 3) {
            int heure = (new Integer(0)).parseInt(tabDuree[0]);
            int minute = (new Integer(0)).parseInt(tabDuree[1]);
            int seconde = (new Integer(0)).parseInt(tabDuree[2]);
            return ((heure * 3600) + (minute * 60) + seconde) * 1000;
        }
        int minute = (new Integer(0)).parseInt(tabDuree[0]);
        int seconde = (new Integer(0)).parseInt(tabDuree[1]);
        return ((minute * 60) + seconde) * 1000;
    }

    int comparer(DateHeure d2) {
        if (comparerHeure(d2) == 0 && comparerDt(d2) == 0) {
            return 0;
        }
        return -1;
    }

    public int comparerHeure(DateHeure d2) {
        if (getHeure() > d2.getHeure()) {
            return 1;
        } else if (getHeure() < d2.getHeure()) {
            return -1;
        } else {
            if (getMinute() > d2.getMinute()) {
                return 1;
            } else if (getMinute() < d2.getMinute()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    int comparerDt(DateHeure d2) {
        if (getAnnee() > d2.getAnnee()) {
            return 1;
        } else if (getAnnee() < d2.getAnnee()) {
            return -1;
        } else {
            if (getMois() > d2.getMois()) {
                return 1;
            } else if (getMois() < d2.getMois()) {
                return -1;
            } else {
                if (getJour() > d2.getJour()) {
                    return 1;
                } else if (getJour() < d2.getJour()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public String jourDeSemaine() {
        GregorianCalendar gc = new GregorianCalendar(getAnnee(), getMois() - 1, getJour(), getHeure(), getMinute());
        return jours()[gc.get(Calendar.DAY_OF_WEEK) - 1];
    }

    String[] jours() {
        String[] jours = new String[7];
        jours[0] = "dimanche";
        jours[1] = "lundi";
        jours[2] = "mardi";
        jours[3] = "mercredi";
        jours[4] = "jeudi";
        jours[5] = "vendredi";
        jours[6] = "samedi";
        return jours;
    }

    public String getFull() {
        return getDate().concat(" ").concat(getTime());
    }

    public String getFullComp() {
        String dt = getDate().substring(0, getDate().length() - 2);
        return dt;
    }

    public void ajouterMinute(int min) {
        setMinute(getMinute() + min);
    }

    public void ajouterJour(int j) {
        GregorianCalendar gc = new GregorianCalendar(getAnnee(), getMois() - 1, getJour());
        gc.add(Calendar.DAY_OF_MONTH, j);

        setJour(gc.get(gc.DAY_OF_MONTH));
        setMois(gc.get(gc.MONTH) + 1);
        setAnnee(gc.get(gc.YEAR));
    }

    public DateHeure getMardiJstAprs() {
        GregorianCalendar gc = new GregorianCalendar(getAnnee(), getMois() - 1, getJour());
        while (gc.get(gc.DAY_OF_WEEK) != Calendar.TUESDAY) {
            gc.add(Calendar.DAY_OF_MONTH, 1);
        }
        DateHeure newDt = new DateHeure();
        newDt.setJour(gc.get(gc.DAY_OF_MONTH));
        newDt.setMois(gc.get(gc.MONTH) + 1);
        newDt.setAnnee(gc.get(gc.YEAR));
        return newDt;
    }

    public DateHeure getMecrediJstAprs() {
        GregorianCalendar gc = new GregorianCalendar(getAnnee(), getMois() - 1, getJour());
        while (gc.get(gc.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
            gc.add(Calendar.DAY_OF_MONTH, 1);
        }
        DateHeure newDt = new DateHeure();
        newDt.setJour(gc.get(gc.DAY_OF_MONTH));
        newDt.setMois(gc.get(gc.MONTH) + 1);
        newDt.setAnnee(gc.get(gc.YEAR));
        return newDt;
    }

    public void setHeure(int val) {
        heure = val;
    }

    public void setMinute(int val) {
        minute = val;
    }

    public void setSeconde(int val) {
        seconde = val;
    }

    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconde() {
        return seconde;
    }

    public void setJour(int val) {
        jour = val;
    }

    public void setMois(int val) {
        mois = val;
    }

    public void setAnnee(int val) {
        annee = val;
    }

    public int getJour() {
        return jour;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }
}
