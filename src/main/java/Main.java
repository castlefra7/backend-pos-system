
import restaurantmanager.Server;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.serverListen(55555);

        //JSONData jsonData = new JSONData();
        //String result  = jsonData.getBigStatistic(11, 2021);
        //System.out.println(result);
        /*   ConnGen connGen =new ConnGen();
        BigStatistic statistic = new AppData().getStatistics(connGen, 5, 2020);
        LineData[] lineData = statistic.getLineData();
        connGen.close();
        for(LineData l: lineData) {
            System.out.println(l.getIndex() + " " + l.getData());
        }

         */
        // JWT AUTHENTICATION
        /*  HashMap<String, String> hm = new HashMap<String, String>();  //implements map interface
        hm.put("userID","125");

        HashMap<String, String> hm1 = new HashMap<String, String>();  //implements map interface
        hm.put("userName","26/05/2020");

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
//        System.out.println(jws);
//        System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject().equals("Joe"));
        String jws = Jwts.builder() // (1)
                .setIssuer("Justin")
                .claim("userID", "125")
                .signWith(key)          // (3)
                .compact();
        System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws));

         */
    }
}
