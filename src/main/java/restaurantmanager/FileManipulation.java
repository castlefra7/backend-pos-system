package restaurantmanager;

public class FileManipulation {
    String getImageName(String json) {

        if(json.length() > 2) {
            String resultF = json.substring(1, json.length() - 1);
            /*char[] all = resultF.toCharArray();
            for(int i = 0; i < all.length; i++) {
                if(all[i] == ' ')
                    all[i] = '_';
            }
            resultF = new String(all);*/
            if(resultF.contains(".")) {
                String[] result = resultF.split("\\.");
                return result[0];
            }
            return resultF;
        }
        return "";
    }

    String getExtension(String json) {
        if(json.contains(".")) {
            String[] sp = json.split("\\.");
            if(sp[1].contains("\""))
                return "." + sp[1].substring(0, sp[1].length() - 1);
            return "." + sp[1];
        } else {
            return ".jgp";
        }
    }
}
