package com.example.fantaf1.network;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fantaf1.buisness_logic.Gestore;
import com.example.fantaf1.classes.Constructor;
import com.example.fantaf1.classes.FastestLap;
import com.example.fantaf1.classes.GrandPrix;
import com.example.fantaf1.classes.Pilota;
import com.example.fantaf1.classes.Standing;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class F1APIservice {

    private static final String HOST = "https://www.formula1.com/";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";

    private final AppCompatActivity context;

    public F1APIservice(Gestore aG){
        context = aG.getContext();
    }

     /**                                **\
     |*                                  *|
     |*            WEBSCRAPER            *|
     |*                                  *|
     \**                                **/

    public void scrapeData(){
        Year thisYear = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            thisYear = Year.now();
        }
        String year = String.valueOf(thisYear);

        Thread thread1 = new Thread(()-> getGrandprix(HOST+"en/results.html/" + year + "/races.html"));
        Thread thread2 = new Thread(()-> getF1Teams(HOST+"en/teams.html"));
        Thread thread3 = new Thread(()-> getPilots(HOST + "en/drivers.html"));
        Thread thread4 = new Thread(()-> getFastestLap(HOST + "en/results.html/" + year + "/fastest-laps.html"));

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try{
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateStandings(){
        Year thisYear = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            thisYear = Year.now();
        }
        String year = String.valueOf(thisYear);

        Thread thread1 = new Thread(()-> getGrandprix(HOST+"en/results.html/" + year + "/races.html"));
        Thread thread4 = new Thread(()-> getFastestLap(HOST + "en/results.html/" + year + "/fastest-laps.html"));

        thread1.start();
        thread4.start();

        try{
            thread1.join();
            thread4.join();
        }catch(Exception ignored){}
    }

    private void writeJson(String fileName,Object o){
        context.runOnUiThread(()->{
            File f = new File(context.getFilesDir(),fileName);
            try (FileWriter myWriter = new FileWriter(f)) {
                myWriter.write(new Gson().toJson(o));
            } catch (IOException ignored) {}
        });
    }

    private void getGrandprix(String url) {
        Document doc = null;

        try {
            doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .header("Accepted-Language", "*")
                    .get();
        } catch (Exception e) {
            //TODO: handle exception
        }

        List<GrandPrix> gs = new ArrayList<>();

        Element products = doc.select("table").get(0);
        Element tbody = products.select("tbody").get(0);

        Elements rows = tbody.select("tr");

        for (Element row : rows) {
            GrandPrix g = new GrandPrix();

            Elements cols = row.select("td");
            g.setUrl(cols.get(1).select("a").get(0).attr("href"));
            g.setName(cols.get(1).text());
            g.setDate(cols.get(2).text());

            gs.add(g);
        }

        for(int i = 0; i < gs.size(); i++){

            try {
                doc = Jsoup.connect(HOST+gs.get(i).getUrl())
                        .userAgent(USER_AGENT)
                        .header("Accepted-Language", "*")
                        .get();
            } catch (Exception e) {
                //TODO: handle exception
            }

            ArrayList<Standing> standings = new ArrayList<>();

            products = doc.select("table").get(0);
            tbody = products.select("tbody").get(0);

            rows = tbody.select("tr");

            for (Element row : rows) {
                Standing s = new Standing();

                Elements cols = row.select("td");
                s.setPos(cols.get(1).text());
                s.setNumber(cols.get(2).text());
                s.setName(cols.get(3).select("span").get(0).text());
                s.setSecondName(cols.get(3).select("span").get(1).text());
                s.setCode(cols.get(3).select("span").get(2).text());
                s.setConstructor(cols.get(4).text());
                s.setLaps(cols.get(5).text());
                s.setTime(cols.get(6).text());

                standings.add(s);
            }
            gs.get(i).setStandings(standings);
        }

        writeJson("grandPrix.json",gs);
    }

    private void getF1Teams(String url){
        long mill = System.currentTimeMillis();
        Document doc = null;

        try {
            doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .header("Accepted-Language", "*")
                    .get();
        } catch (Exception e) {
            //TODO: handle exception
        }

        List<String> urls = new ArrayList<>();

        Element main = doc.select("main").get(0);

        Elements out = main.select("div").get(14).children();

        for (Element row : out){
            String u = row.select("div").get(0).select("a").get(0).attr("href");
            urls.add(u);
        }

        List<Constructor> cons = new ArrayList<>();

        for (String u : urls){
            try {
                doc = Jsoup.connect(HOST+u)
                        .userAgent(USER_AGENT)
                        .header("Accepted-Language", "*")
                        .get();
            } catch (Exception e) {
                //TODO: handle exception
            }

            Element table = doc.select("table").get(0);
            Element tbody = table.select("tbody").get(0);

            Elements rows = tbody.select("tr");

            Constructor c = new Constructor();

            c.setName(rows.get(0).select("td").get(0).text());
            c.setBase(rows.get(1).select("td").get(0).text());
            c.setTeamChief(rows.get(2).select("td").get(0).text());
            c.setTechChief(rows.get(3).select("td").get(0).text());
            c.setChassis(rows.get(4).select("td").get(0).text());
            c.setPowUnit(rows.get(5).select("td").get(0).text());
            c.setFirstTeamEntry(rows.get(6).select("td").get(0).text());
            c.setWorldChamps(rows.get(7).select("td").get(0).text());
            c.setPolePos(rows.get(9).select("td").get(0).text());
            c.setFastestlap(rows.get(10).select("td").get(0).text());

            cons.add(c);
        }

        writeJson("constructors.json",cons);
        System.out.println("\ntime elapsed parsing constructors:\t"+(System.currentTimeMillis()-mill));
    }

    private void getPilots(String url){
        Document doc = null;

        try {
            doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .header("Accepted-Language", "*")
                    .get();
        } catch (Exception e) {
            //TODO: handle exception
        }

        Elements out = doc.select("main").get(0).select("div").get(14).children();

        List<Pilota> pilots = new ArrayList<>();

        for (Element row : out){
            try {
                doc = Jsoup.connect(HOST+row.select("div").get(0).select("a").get(0).attr("href"))
                        .userAgent(USER_AGENT)
                        .header("Accepted-Language", "*")
                        .get();
            } catch (Exception ignored) {}

            Elements rows = doc.select("table").get(0).select("tbody").get(0).select("tr");

            Pilota p = new Pilota();

            p.setTeam(rows.get(0).select("td").get(0).text());
            p.setNationality(rows.get(1).select("td").get(0).text());
            p.setPodiums(rows.get(2).select("td").get(0).text());
            p.setGrandPrixEntered(rows.get(4).select("td").get(0).text());
            p.setWorldChamps(rows.get(5).select("td").get(0).text());
            p.setDateOfBirth(rows.get(8).select("td").get(0).text());
            p.setPlaceOfBirth(rows.get(9).select("td").get(0).text());

            Element figCap = doc.select("main").get(0).selectFirst("figcaption");

            p.setNumber(figCap.select("div").get(0).select("span").get(0).text());
            p.setName(figCap.select("h1").get(0).text());

            pilots.add(p);
        }

        writeJson("drivers.json",pilots);
    }

    private void getFastestLap(String url) {
        Document doc = null;

        try {
            doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .header("Accepted-Language", "*")
                    .get();
        } catch (Exception e) {
            //TODO: handle exception
        }

        List<FastestLap> fls = new ArrayList<>();

        Element tbody = doc.select("table").get(0).select("tbody").get(0);

        Elements rows = tbody.select("tr");

        for (Element row : rows) {
            FastestLap fl = new FastestLap();

            Elements cols = row.select("td");
            fl.setGrandPrix(cols.get(1).text());
            fl.setDriver(cols.get(2).text());
            fl.setCar(cols.get(3).text());
            fl.setTime(cols.get(4).text());

            fls.add(fl);
        }

        writeJson("fastestsLap.json",fls);
    }

    /*
    public ArrayList<Object>  fetchData(){
        start = System.currentTimeMillis();
        sendMessage("scaricando i costruttori...");
        interrogateUrl(0,"constructors","https://ergast.com/api/f1/current/constructors.json");
        sendMessage("scaricando i piloti...");
        for(int i = 0; i < constructors.size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"piloti","https://ergast.com/api/f1/current/constructors/"+constructors.get(i).getConstructorId()+"/drivers.json",constructors.get(i).getConstructorId());
        }
        sendMessage("scaricando i piazzamenti...");
        for(int i = 0; i < pilots.size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"standings","https://ergast.com/api/f1/current/drivers/"+ pilots.get(i).getDriverId() +"/results.json");
        }
        ArrayList<Object> pilotsANDcons = new ArrayList<>();
        pilotsANDcons.add(pilots);
        pilotsANDcons.add(constructors);
        return pilotsANDcons;
    }

    public ArrayList<Object> updateStandings() {
        sendMessage("aggiornando i piazzamenti...");
        for(int i = 0; i < g.getPilots().size(); i++){
            start = System.currentTimeMillis();
            interrogateUrl(i,"updatestandings","https://ergast.com/api/f1/current/drivers/"+ g.getPilots().get(i).getDriverId() +"/results.json");
        }
        ArrayList<Object> pilotsANDcons = new ArrayList<>();
        pilotsANDcons.add(pilots);
        pilotsANDcons.add(constructors);
        return pilotsANDcons;
    }

    private void sendMessage(String aMessage) {
        context.runOnUiThread(()->{
            TextView t = context.findViewById(R.id.message);
            t.setText(aMessage);
        });
    }

    private void interrogateUrl(int i,String... q){
        try{
            url = new URL(q[1]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            responseCode = conn.getResponseCode();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (responseCode != 200) {
                records += "CONNECTION FAILED:\t" + responseCode;
            } else if (url != null) {
            switch (q[0]) {
                case "constructors" -> constructorsParsing();
                
                case "piloti" -> pilotsParsing(q[2]);
                
                case "standings" -> standingsParsing(i);
                
                case "updatestandings" -> updateS(i);
                default -> {}
            }
        }
        url = null;
    }

    private void updateS(int i) {
        try {
            JSONObject obj = parseJson();
            //Get the required object from the above created object

            JSONArray races = (JSONArray) (getObject(getObject(obj, "MRData"), "RaceTable")).get("Races");

            g.getPilots().get(i).addStanding(addS((JSONObject) races.get(races.length() - 1)));
        }catch(Exception ex){}

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex){}
        }
    }
    private static Standing addS(JSONObject obj){
        try {
            return new Standing(obj.get("raceName").toString(),
                    ((JSONObject) ((JSONArray) obj.get("Results")).get(0)).get("position").toString(),
                    obj.get("date").toString(),
                    new Circuit(((JSONObject) obj.get("Circuit")).get("circuitId").toString(),
                            ((JSONObject) obj.get("Circuit")).get("circuitName").toString(),
                            new Location(((JSONObject) ((JSONObject) obj.get("Circuit")).get("Location")).get("locality").toString(),
                                    ((JSONObject) ((JSONObject) obj.get("Circuit")).get("Location")).get("country").toString()
                            )
                    )
            );
        }catch(Exception ex){
            return null;
        }
    }

    private JSONObject parseJson(){
        JSONObject data_obj = null;
        Scanner scanner;
        records = "";
        try {
            scanner = new Scanner(url.openStream());//Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                records += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();

            //Using the JSON simple library parse the string into a json object
            data_obj = new JSONObject(records);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return data_obj;
    }

    private void pilotsParsing(String constructor){
        JSONObject obj = parseJson();
        //Get the required object from the above created object

        JSONArray drivers = null;
        try {
            drivers = (JSONArray)(getObject(getObject(obj,"MRData"),"DriverTable")).get("Drivers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < drivers.length(); j++) {
            try {
                addPilot((JSONObject) drivers.get(j), constructor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void standingsParsing(int i){
        standings = new ArrayList<>();
        JSONObject obj = parseJson();
        //Get the required object from the above created object

        JSONArray races = null;
        try {
            races = (JSONArray) (getObject(getObject(obj,"MRData"),"RaceTable")).get("Races");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < races.length(); j++) {
            try {
                addStanding((JSONObject) races.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        long delay = MINTIME - (System.currentTimeMillis()-start);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        pilots.get(i).setStandings(standings);
        standings = null;
    }

    private void constructorsParsing() {
        JSONObject obj = parseJson();

        JSONArray constrs = null;
        try {
            constrs = (JSONArray)(getObject(getObject(obj,"MRData"),"ConstructorTable")).get("Constructors");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < constrs.length(); j++) {
            try {
                addConstructor((JSONObject) constrs.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject getObject(JSONObject obj, String key) throws JSONException {
        return (JSONObject)obj.get(key);
    }

    private void addPilot(JSONObject obj, String constructor){
        try {
            pilots.add(new Pilota(obj.get("driverId").toString(),
                            constructor,
                            obj.get("code").toString(),
                            Integer.parseInt(obj.get("permanentNumber").toString()),
                            obj.get("nationality").toString(),
                            obj.get("givenName").toString(),
                            obj.get("familyName").toString(),
                            obj.get("dateOfBirth").toString()
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addStanding(JSONObject obj){
        try {
            standings.add(new Standing(obj.get("raceName").toString(),
                            ((JSONObject)((JSONArray)obj.get("Results")).get(0)).get("position").toString(),
                            obj.get("date").toString(),
                            new Circuit(((JSONObject)obj.get("Circuit")).get("circuitId").toString(),
                                    ((JSONObject)obj.get("Circuit")).get("circuitName").toString(),
                                    new Location(((JSONObject)((JSONObject)obj.get("Circuit")).get("Location")).get("locality").toString(),
                                            ((JSONObject)((JSONObject)obj.get("Circuit")).get("Location")).get("country").toString()
                                    )
                            )
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addConstructor(JSONObject obj) {
        try {
            constructors.add(new Constructor(obj.get("constructorId").toString(),
                            obj.get("name").toString(),
                            obj.get("nationality").toString()
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
