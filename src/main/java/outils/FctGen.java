package outils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.lang.reflect.Method;
import java.util.Vector;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSet;
import java.sql.Statement;

import connexion.ConnGen;
import temps.DateHeure;

public class FctGen
{

	public boolean isNumber(String s) {
		try {
			Double dbl = new Double(s);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public int[] allNumbers() {
		int[] res = new int[10];
		for(int iR = 0; iR < res.length; iR++) {
			res[iR] = iR;
		}
		return  res;
	}

	public boolean isNumber(char c) {
		int[] allNumb = allNumbers();
		for(int iN = 0; iN < allNumb.length; iN++) {
			if(Character.toString(c).compareTo(String.valueOf(iN)) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean containsNumber(String str) {
		char[] strC = str.toCharArray();
		for(int iC = 0; iC < strC.length; iC++) {
			if(isNumber(strC[iC])) {
				return true;
			}
		}
		return false;
	}

	public String findOne(String req, String colName) throws  Exception {
		return findOne(new ConnGen(), req, colName);
	}

	public String findOne(ConnGen cGen, String req, String colName) throws Exception {
		Statement state = cGen.getConn().createStatement();
		try {
			ResultSet result = state.executeQuery(req);
			
			if(result.next()) {
				if(result.getString(colName) == null) return "";
				return result.getString(colName);
			}
			state.close();
			result.close();
			return "";

		} catch(Exception ex) {
			if(ex.getMessage().contains("ORA-08002")) {
				return "";
			}
			throw ex;
		}
	}

	public Object[] findAllArr(Object base, String[] req, String idBase) throws Exception
	{
		ConnGen cGen = new ConnGen();
		Object[] result = findAllArr(cGen, base, req, idBase);
		cGen.close();
		return result;
	}

	public Object[] findAll(Object base, String req) throws Exception
	{
		ConnGen cGen = new ConnGen();
		Object[] result = findAll(cGen, base, req);
		cGen.close();
		return result;
	}

	public float findFloat(String req, String colName) throws  Exception {
		ConnGen connGen = new ConnGen();
                Statement state = connGen.getConn().createStatement();
		ResultSet result = state.executeQuery(req);
		float val = 0;
		if(result.next()) {
                    val = result.getFloat(colName);
		}
		state.close();
		result.close();
		connGen.close();
		return val;
	}

	public String findStr(String req, String colName) throws  Exception {
		ConnGen connGen = new ConnGen();
		String val =  findStr(connGen, req, colName);

		connGen.close();
		return val;
	}

	public String findStr(ConnGen connGen,String req, String colName) throws  Exception {

		ResultSet result = connGen.getResultSet(req);
		String val = null;
		if(result.next()) {
			val = result.getString(colName);
		}
		result.close();
		return val;
	}

	public Object[] findAllArr(ConnGen cGen, Object base, String[] req, String idBase) throws Exception 
	{
		ResultSet result = cGen.getResultSet(req[0]);

		Class clsBase = base.getClass();
		Class[] clsConstruct = null;
		Method[] allMtd = getAllMethods(base, "set");

		int ind = 0;
		String mtdArrName = null;
		for(int iMtd = 0; iMtd < allMtd.length; iMtd++)
		{
			Class[] params = allMtd[iMtd].getParameterTypes();
			if(params[0].isArray())
			{
				ind = iMtd;
				mtdArrName = new String(allMtd[iMtd].getName().substring(3).toUpperCase());
				break;
			}
		}
		Vector allRows = new Vector();

		Object secBase = allMtd[ind].getParameterTypes()[0].getComponentType().getConstructor(clsConstruct).newInstance();

		while(result.next())
		{
			Object newOb = clsBase.getConstructor(clsConstruct).newInstance();
			for(int iM = 0; iM < allMtd.length; iM++)
			{
				String mtdSpName = allMtd[iM].getName().substring(3).toUpperCase();
				Class[] params = allMtd[iM].getParameterTypes();
				if(params[0].getSimpleName().compareTo("String") == 0 && mtdArrName.compareTo(mtdSpName) != 0)
				{
					if(result.getString(mtdSpName) != null)
					{
						allMtd[iM].invoke(newOb, result.getString(mtdSpName));
					}
				}
				if(mtdArrName.compareTo(mtdSpName) == 0 && iM == ind)
				{
					String secReq = req[1].concat(" ").concat(result.getString(idBase));
					Object[] secAll = findAll(cGen, secBase, secReq);
					Object[] secAllCast = (Object[])Array.newInstance(secBase.getClass(), secAll.length);
					
					for(int iS = 0; iS < secAll.length; iS++)
					{
						secAllCast[iS] = secAll[iS];
					}
					allMtd[ind].invoke(newOb, new Object[]{secAllCast});
				}
			}
			allRows.add(newOb);
		}

		
		result.close();

		return allRows.toArray();
	}

	public Object[] findAll(ConnGen cGen, Object base, String req) throws Exception
	{
		// ALTER SESSION FIRST
		//TODO: How to alter sessino pgsql
		//String req_session = "ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD'";
		//cGen.insertResult(req_session);

		Statement state = cGen.getConn().createStatement();
		ResultSet result = state.executeQuery(req);
		
		int isNull = -1;
		
		Class clsBase = base.getClass();
		Class[] clsConstruct = null;
		Method[] allMtd = getAllMethods(base, "set");
		
		Vector allRows = new Vector();
		
		while(result.next())
		{
			isNull++;
			Object newOb = clsBase.getConstructor(clsConstruct).newInstance();
			for(int iM = 0; iM < allMtd.length; iM++)
			{
				String mtdSpName = allMtd[iM].getName().substring(3).toUpperCase();
				Class[] params = allMtd[iM].getParameterTypes();
				if(params[0].getSimpleName().compareTo("String") == 0)
				{
					if(result.getString(mtdSpName) != null)
					{
						allMtd[iM].invoke(newOb, result.getString(mtdSpName));
					}
				}
			}
			
			allRows.add(newOb);
		}
		state.close();
		result.close();
		if(isNull == -1)
		{
			return null;
		}
		
		return allRows.toArray();
	}

	public int insertBdd(Object base, String nomTable, String seqName, String idName) throws Exception
	{
		ConnGen cGen = new ConnGen();
		int val = insertBdd(cGen, base, nomTable, seqName, idName);
		cGen.getConn().commit();
		cGen.close();
		return val;
	}
	
	public int insertBdd(ConnGen bdd, Object base, String nomTable, String seqName, String idName) throws Exception
	{
		String cols = new String("(");
		String values = new String("(");
		Method[] allMtd = getAllMethods(base, "get");
		Object[] params = null;
		idName = "get" + idName;
		
		for(int iM = 0; iM < allMtd.length; iM++)
		{

			cols += allMtd[iM].getName().substring(3) + ",";
			String returnType = allMtd[iM].getReturnType().getSimpleName();

			
			if(returnType.compareTo("String") == 0)
			{
				
				if(allMtd[iM].getName().toLowerCase().compareTo(idName.toLowerCase()) == 0)
				{
					values += "nextVal('" + seqName + "'),";
				}
				else
				{
		       		values += "'" + (String)allMtd[iM].invoke(base, params) + "',";	
				}
			} else if(returnType.compareTo("DateHeure") == 0)
			{
				DateHeure dt = (DateHeure)allMtd[iM].invoke(base, params);
				
				values += "'" + dt.getDateEn() + "',";
			} else if(returnType.compareTo("boolean") == 0) {
				values += "'" + String.valueOf(((Boolean)allMtd[iM].invoke(base, params)).booleanValue()) + "',";
			} else if(returnType.compareTo("char") == 0) {
				values += "'" + Character.valueOf(((Character)allMtd[iM].invoke(base, params)).charValue()) + "',";
			}
			else
			{
				values += String.valueOf(((Number)allMtd[iM].invoke(base, params)).doubleValue()) + ",";
			}
		}
		String req = "insert into " + nomTable + " " + cols.substring(0, cols.length() - 1) + ") values " + values.substring(0, values.length() -1) + ")";
                
		//TODO: HOW TO ALTER SYSTEM DATA IN POSTGRESQL
		//bdd.insertResult("ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD'");

		int val = bdd.insertResult(req);

		

		return val;
	}

	public Field[] getBasicFields(Object base) throws Exception
	{
		Field[] all = getAllFields(base);
		Vector<Field> allCorr = new Vector();
		for(int iAll = 0; iAll < all.length; iAll++)
		{
			if((all[iAll].getType().isArray() == false) && (all[iAll].getType().isPrimitive() || (all[iAll].getType().getSimpleName().toLowerCase().compareTo("string") == 0)) )
			{
				allCorr.add(all[iAll]);
			}
		}

		Field[] allCorrField = new Field[allCorr.size()];
		return allCorr.toArray(allCorrField);
	}

	public boolean estNombre(Field fd)
	{
		String nomField = fd.getType().getSimpleName().toLowerCase();
		return (nomField.compareTo("double") == 0) || (nomField.compareTo("int") == 0) || (nomField.compareTo("float") == 0);
	}

    public Object trouverObjectMinFct(Object[] lo, String nomFct) throws Exception
    {
		Object val = lo[0];
		Method mtd = getSpecMeth(val.getClass().getMethods(), nomFct);
		Object[] parValueMtd = null;

		for(int iLo = 1; iLo < lo.length; iLo++)
		{
			double min = ((Number)mtd.invoke(val, parValueMtd)).doubleValue();
			double enCours = ((Number)mtd.invoke(lo[iLo], parValueMtd)).doubleValue();
			if(enCours < min)
			{
				val = lo[iLo];
			}
		}
		return val;
    }

    public Object trouverObjectMinAttr(Object[] lo, String nomAttr) throws Exception
    {
		Object val = lo[0];
		Class[] parTypeMtd = null;
		Object[] parValueMtd = null;

		Method mtd = val.getClass().getMethod(creerNomFct("get", nomAttr), parTypeMtd);
		for(int iLo = 1; iLo < lo.length; iLo++)
		{
			double min = ((Number)mtd.invoke(val, parValueMtd)).doubleValue();
			double enCours = ((Number)mtd.invoke(lo[iLo], parValueMtd)).doubleValue();
			if(enCours < min)
			{
				val = lo[iLo];
			}
		}
		return val;
    }

    public Object[] getMinMax(Object[] lstObj, String nomAttr) throws Exception
    {
		Object[] ob = new Object[2];
		Class[] parTypeMtd = null;
		Object[] parValueMtd = null;
		double enCours = 0.0, min = 0.0, max = 0.0;
		String nomFct = creerNomFct("get", nomAttr);
		Method mtd = lstObj[0].getClass().getMethod(nomFct, parTypeMtd);

		ob[0] = lstObj[0];
		ob[1] = lstObj[0];

		for(int iLst = 1; iLst < lstObj.length; iLst++)
		{
			enCours = ((Number)mtd.invoke(lstObj[iLst], parValueMtd)).doubleValue();
			min = ((Number)mtd.invoke(ob[0], parValueMtd)).doubleValue();
			max = ((Number)mtd.invoke(ob[1], parValueMtd)).doubleValue();

			if(enCours < min)
			{
				ob[0] = lstObj[iLst];
			}
			if(enCours > max)
			{
				ob[1] = lstObj[iLst];
			}
		}		
		return ob;
    }

	    
    public Object[][] createData2Dim(Object[] lst, String[] mtdNames) throws Exception
    {
		if(lst == null)
		{
			return null;
		}
        Object[][] val = new Object[lst.length][mtdNames.length];
        Class[] parType = null;
        Object[] parVal = null;

        for(int iLst = 0; iLst < lst.length; iLst++)
        {
            for(int iCol = 0; iCol < mtdNames.length; iCol++)
            {
                Method m = lst[0].getClass().getMethod(creerNomFct("get", mtdNames[iCol]), parType);
				if(mtdNames[iCol].toLowerCase().compareTo("class") == 0)
				{
					val[iLst][iCol] = ((Class)m.invoke(lst[iLst], parVal)).getSimpleName();
				}
				else
				{
                	val[iLst][iCol] = m.invoke(lst[iLst], parVal);
				}
            }
        }
        return val;
	}
	
    
    public boolean inserer(String pathname, Object ob, String sep) throws Exception
    {
		File fichier = new File(pathname);
        Method[] allMtd = getAllMethods(ob, "get");		
        String val = new String();
        Method mtd = null;
        Class[] parTypeMtd = null;
        Object[] parValueMtd = null;
        String nomMtd = null;
		String[] headLines = null;
		
		if(ob.getClass().getSimpleName().toLowerCase().compareTo("string") == 0)
		{
			allMtd = new Method[1];
			allMtd[0] = ob.getClass().getMethod("intern", parTypeMtd);

			if(fichier.exists() == false)
			{
				val = "intern" + '\n';
			}

			val = val + allMtd[0].invoke(ob, parValueMtd) + sep;
			val = val.substring(0, val.length() - sep.length()) + '\n';

			if(ecrireFichier(fichier, val))
			{
				return true;
			}
			return false;	
		}
        
        if(fichier.exists())
        {
			BufferedReader br = new BufferedReader(new FileReader(fichier));
			headLines = br.readLine().split(sep);   
			br.close();
		}
		else
		{
			char c;
			for(int iAll = 0; iAll < allMtd.length; iAll++)
			{
				nomMtd = allMtd[iAll].getName();
				c = nomMtd.charAt(3);
				val = val + (new Character('a')).toLowerCase(c) + nomMtd.substring(4, nomMtd.length()) + sep;
			}
			val = val.substring(0, val.length() - sep.length());
			headLines = val.split(sep);
			val = val + '\n';
		}

		
		int idFichier = 1;
        for(int iHead = 0; iHead < headLines.length; iHead++)
        {
                nomMtd = creerNomFct("get", headLines[iHead]);
				mtd = ob.getClass().getMethod(nomMtd, parTypeMtd);
				if(estPrimitiveType(mtd, "get"))
				{
					val = val + mtd.invoke(ob, parValueMtd) + sep;
				}
				else if(mtd.getReturnType().isArray())
				{
					String id = (String)ob.getClass().getMethod("getIdentifiant", parTypeMtd).invoke(ob, parValueMtd);
					inserer(id.concat(String.valueOf(idFichier)).concat(".txt"), (Object[])mtd.invoke(ob, parValueMtd), sep);

					val = val + id.concat(String.valueOf(idFichier)).concat(".txt") + sep;
					idFichier++;
				}
				else
				{
					if(mtd.invoke(ob, parValueMtd) == null)
					{
						val = val + "null" + sep;
					}
					else
					{
						String id = (String)ob.getClass().getMethod("getIdentifiant", parTypeMtd).invoke(ob, parValueMtd);
						inserer(id.concat(String.valueOf(idFichier)).concat(".txt"), mtd.invoke(ob, parValueMtd), sep);
						val = val + id.concat(String.valueOf(idFichier)).concat(".txt") + sep;	
						idFichier++;
					}
				}
        }
        val = val.substring(0, val.length() - sep.length()) + '\n';

        if(ecrireFichier(fichier, val))
        {
            return true;
        }
        return false;		
    }
    
    public Object[] findAll(String pathname, Object base, String sep) throws Exception
    {
        int totalTrouver = nbreLigne(pathname);
		if(totalTrouver == -1)
		{
			return null;
		}
        Object[] val = new Object[totalTrouver];
        int iVal = 0;
        File fs = new File(pathname);
        String venantFich = null;
        
        if(fs.exists())
        {
			BufferedReader br = new BufferedReader(new FileReader(fs));
 
            String[] headLines = br.readLine().split(sep);
            while((venantFich = br.readLine()) != null)
            {
				val[iVal] = creerObject(base, venantFich.split(sep), headLines, sep);
				iVal++;
            }
            br.close();
        }
		else
		{
			return null;
		}
        return val;
    }

    public Object getSomme(Object[] lstObj, String nomAttr) throws Exception
    {
		if(lstObj == null)
		{
			return 0.0;
		}
		if(lstObj.length == 0)
		{
			return 0.0;
		}
		Field[] all = getAllFields(lstObj[0]);
		Field fd = getSpecField(all, nomAttr);
		Class[] parTypeMtd = null;
		Object[] parValueMtd = null;
		Method mtd = lstObj[0].getClass().getMethod(creerNomFct("get", nomAttr), parTypeMtd);
		if(fd.getType().getSimpleName().toLowerCase().compareTo("dateheure") == 0)
		{
			DateHeure enCours = null;
			DateHeure dt = new DateHeure();
			for(int iLst = 0; iLst < lstObj.length; iLst++)
			{
				enCours = (DateHeure)mtd.invoke(lstObj[iLst], parValueMtd);
				dt = dt.ajouter(enCours);
			}
			return dt;
		}
		double val = 0.0;
		double enCours = 0.0;
		for(int iLst = 0; iLst < lstObj.length; iLst++)
		{
			enCours = ((Number)mtd.invoke(lstObj[iLst], parValueMtd)).doubleValue();
			val = val + enCours;
		}
		return val;
		
    }

    public String creerNomFct(String prefixe, String nomAttr)
    {
		String val;
		char[] tbNom = nomAttr.toCharArray();
		Character first = tbNom[0];
		val = prefixe + first.toUpperCase(first.charValue()) + nomAttr.substring(1, nomAttr.length());
		return val;
    }

    Method getSpecMeth(Method[] lstMeth, String methodName)
    {
		int taille = lstMeth.length;
		for(int iLst = 0; iLst < taille; iLst++)
		{
			if(lstMeth[iLst].getName().compareTo(methodName) == 0)
			{
				return lstMeth[iLst];
			}
		}
		return null;
    }
	
    public Method[] getAllMethods(Object ob, String prefix)
    {
		Method[] allMtd = ob.getClass().getMethods();
		int taille = 0;
		for(int iMtd = 0; iMtd < allMtd.length; iMtd++)
		{
			String mtdName = allMtd[iMtd].getName();
			if(mtdName.startsWith(prefix) && (mtdName.compareTo("getClass") != 0))
			{
				taille++;
			}
		}
		
		Method[] val = new Method[taille];
		int iVal = 0;
		for(int iMtd = 0; iMtd < allMtd.length; iMtd++)
		{
			String mtdName = allMtd[iMtd].getName();
			if(mtdName.startsWith(prefix) && (mtdName.compareTo("getClass") != 0))
			{
				val[iVal] = allMtd[iMtd];
				iVal++;
			}
		}

		return val;
    }
	
	boolean estPrimitiveType(Method mtd, String prefix)
	{
		String typeName;
		if(prefix.toLowerCase().compareTo("set") == 0)
		{
			typeName = mtd.getParameterTypes()[0].getSimpleName().toLowerCase();
		}
		else
		{
			typeName = mtd.getReturnType().getSimpleName().toLowerCase();
		}
		return (typeName.compareTo("double") == 0) || (typeName.compareTo("int") == 0) ||
			(typeName.compareTo("integer") == 0) || (typeName.compareTo("string") == 0) || (typeName.compareTo("char") == 0)
			|| (typeName.compareTo("character") == 0) || (typeName.compareTo("float") == 0);
	}
	
	public Class[] getAllMethodParameterType(Method[] allMtd)
	{
		Class[] val = new Class[allMtd.length];
		for(int iVal = 0; iVal < val.length; iVal++)
		{
			val[iVal] = allMtd[iVal].getParameterTypes()[0];
		}
		return val;
	}
	
	public Method[] getAllValidMethod(Object ob, String prefix)
	{
		Method[] allMtd = getAllMethods(ob, prefix);
		Vector<Method> allValidMethod = new Vector<Method>();
		for(int iAll = 0; iAll < allMtd.length; iAll++)
		{
			allValidMethod.add(allMtd[iAll]);
		}
		
		Method[] val = new Method[allValidMethod.size()];
		for(int iVal = 0; iVal < val.length; iVal++)
		{
			val[iVal] = allValidMethod.elementAt(iVal);
		}
		return val;
	}

	public boolean classNameMethodExist(Object base)
	{
		Method[] allValidMethod = getAllValidMethod(base, "get");
		for(int iAll = 0; iAll < allValidMethod.length; iAll++)
		{
			if(allValidMethod[iAll].getName().toLowerCase().compareTo("getclassname") == 0)
			{
				return true;
			}
		}
		return false;
	}

	public int getClassName(String[] headLines)
	{
		for(int iH = 0; iH < headLines.length; iH++)
		{
			if(headLines[iH].toLowerCase().compareTo("classname") == 0)
			{
				return iH;
			}
		}
		return -1;
	}

	public Object getObject(DefaultTableModel tableModel, Integer id, Object base)
    {
		FctGen gen = new FctGen();

		String[] allCols = new String[tableModel.getColumnCount()];
		for(int iCol = 0; iCol < allCols.length; iCol++)
		{
			allCols[iCol] = tableModel.getColumnName(iCol);
		}

        Object[] values = ((Vector)tableModel.getDataVector().elementAt(id)).toArray();
        String[] colName = new String[tableModel.getColumnCount()];
        String[] colValue = new String[tableModel.getColumnCount()];

        for(int iCol = 0; iCol < tableModel.getColumnCount(); iCol++)
        {
            colName[iCol] = allCols[iCol];
        }
        for(int iColVal = 0; iColVal < tableModel.getColumnCount(); iColVal++)
        {
            if(values[iColVal].getClass().getSimpleName().toLowerCase().compareTo("string") == 0)
            {
                colValue[iColVal] = (String)values[iColVal];
            }
            else
            {
                colValue[iColVal] = new String().valueOf(((Number)values[iColVal]).intValue());
            }
        }

        Object val = null;
        try
        {
            val = gen.creerObject(base, colValue, colName, ";;");
        }catch(Exception e){}

        return val;
    }
    
    public Object creerObject(Object base, String[] line, String[] headLines, String sep) throws Exception
    {
		Class[] clsConstruct = null;
		Object val = base.getClass().getConstructor(clsConstruct).newInstance();		
		Method[] allValidMethod = getAllValidMethod(base, "set");
		Class[] allParameterType = getAllMethodParameterType(allValidMethod);
		Class[] parmType = null;

		if(base.getClass().getSimpleName().toLowerCase().compareTo("string") == 0)
		{
			String val1 = new String(line[0]);
			return val1;
		}

		boolean clsNameExist = classNameMethodExist(base);
		if(clsNameExist == true)
		{
			int indClass = getClassName(headLines);
			if(indClass != -1)
			{
				String classNameValue = line[indClass];
				val = base.getClass().forName(classNameValue).getConstructor(clsConstruct).newInstance();
			}
		}
		
		for(int iHead = 0; iHead < headLines.length; iHead++)
		{
			Class[] cls = new Class[1];
			Object[] args = new Object[1];
			int iMtd = indexMethod(allValidMethod, headLines[iHead]);
			if(iMtd != -1)
			{
				cls[0] = allParameterType[iMtd];
				Method mtd = base.getClass().getMethod(allValidMethod[iMtd].getName(),cls);
				if(cls[0].isArray())
				{
					Object baseArr = cls[0].getComponentType().getConstructor(clsConstruct).newInstance();
					Object[] all = findAll(line[iHead], baseArr, sep);
					Object[] arg = (Object[])Array.newInstance(baseArr.getClass(), all.length);
					for(int iArg = 0; iArg < all.length; iArg++)
					{
						arg[iArg] = all[iArg];
					}
					mtd.invoke(val, new Object[]{arg});
				}
				else if (estPrimitiveType(mtd, "set"))
				{
					args[0] = conversion(allParameterType[iMtd].getSimpleName(), line[iHead]);
					mtd.invoke(val, args);
				}
				else
				{
					if(line[iHead].toLowerCase().compareTo("null") == 0)
					{
						args[0] = null;
						mtd.invoke(val, args);
					}
					else
					{
						Object sousBase = cls[0].getConstructor(clsConstruct).newInstance();
						Object[] allObj = findAll(line[iHead], sousBase, sep);
						args[0] = allObj[0];
						mtd.invoke(val, args);
					}
				}
			}
		}
		return val;
	}

	
	
	int indexMethod(Method[] allValidMethod, String nomAttr)
	{
		nomAttr = nomAttr.toLowerCase();
		for(int iMtd = 0; iMtd < allValidMethod.length; iMtd++)
		{
			String nomMtd = allValidMethod[iMtd].getName();
			nomMtd = nomMtd.substring(3, nomMtd.length()).toLowerCase();
			if(nomMtd.compareTo(nomAttr) == 0)
			{
				return iMtd;
			}
		}
		return -1;
	}
	
	public double getMoyenne(Object[] lo, String nomAttr) throws Exception
    {
        return ((Number)getSomme(lo, nomAttr)).doubleValue() / (double)lo.length;
    }
	
	Object conversion(String typeName, String valeur)
	{
		switch(typeName.toLowerCase())
		{
			case "int": case "integer":
				return (new Integer(0)).parseInt(valeur);
			case "double":
				return (new Double(0.0)).parseDouble(valeur);
			case "float": case "Float":
				return (new Float(0.0)).parseFloat(valeur);
			default:
				return valeur;
		}
	}
    
    int nbreLigne(String pathname) throws Exception
    {
        File f = new File(pathname);
        int val = 0;
        if(f.exists())
        {
            FileReader fr = new FileReader(f);
            BufferedReader bf = new BufferedReader(fr);
            String s = null;
            while((s = bf.readLine()) != null)
            {
                val++;
            }
        }
        return val - 1;
    }
	
	public boolean inserer(String pathname, Object[] listObj, String sep) throws Exception
	{
		for(int iOb = 0; iOb < listObj.length; iOb++)
		{
			if(!inserer(pathname, listObj[iOb], sep))
			{
				return false;
			}
		}
		return true;
	}

    public boolean ecrireFichier(String nomFichier, String texte) throws Exception
    {
		File fichier = new File(nomFichier);
		if(fichier.exists() == false)
		{
			fichier.createNewFile();
		}
        FileWriter fw = new FileWriter(fichier, true);
        fw.write(texte);
        fw.close();
        return true;
    }

	public boolean ecrireFichier(File fichier, String texte) throws Exception
    {
		if(fichier.exists() == false)
		{
			fichier.createNewFile();
		}
        FileWriter fw = new FileWriter(fichier, true);
        fw.write(texte);
        fw.close();
        return true;
    }

	public void triObject(Object[] lo, String nomAttr, boolean asc) throws Exception
	{
		if(lo == null)
		{
			throw new Exception("Liste object ne peut pas etre null");
		}
		if(lo.length == 0)
		{
			return;
		}
		int j;
		Object temp = lo[0].getClass().getConstructor().newInstance();
		Class[] parTypeMtd = null;
		Object[] parValueMtd = null;
		Method mtd =  lo[0].getClass().getMethod(creerNomFct("get", nomAttr), parTypeMtd);
		int tailleLo = lo.length;
		for(int iLo = 0; iLo < (tailleLo - 1); iLo++)
		{
			for(j = 0; j < (tailleLo - 1 - iLo); ++j)
			{
				double valJ = ((Number)mtd.invoke(lo[j], parValueMtd)).doubleValue();
				double valJPus1 = ((Number)mtd.invoke(lo[j+1], parValueMtd)).doubleValue();
				if(asc)
				{
					if(valJ > valJPus1)
					{
						temp = lo[j+1];
						lo[j+1] = lo[j];
						lo[j] = temp;
					}
				}
				else
				{
					if(valJ < valJPus1)
					{
						temp = lo[j+1];
						lo[j+1] = lo[j];
						lo[j] = temp;
					}
				}

			}
		}
	}

	public Field[] getAllFields(Object base) throws Exception
	{
		int taille = 0;
		Class cls = base.getClass();
		while(cls.getSimpleName().toLowerCase().compareTo("object") != 0)
		{
			taille = taille + cls.getDeclaredFields().length;
			cls = cls.getSuperclass();
		}
		
		Field[] all = new Field[taille];
		int iAll = 0;
		cls = base.getClass();
		while(cls.getSimpleName().toLowerCase().compareTo("object") != 0)
		{
			for(int i = 0; i < cls.getDeclaredFields().length; i++)
			{
				all[iAll] = cls.getDeclaredFields()[i];
				iAll++;
			}
			cls = cls.getSuperclass();
		}
		return all;
	}

    public Field getSpecField(Field[] allField, String nom) throws Exception
	{
		for(int i = 0; i < allField.length; i++)
		{
			if(allField[i].getName().compareTo(nom) == 0)
			{
				return allField[i];
			}
		}
		return null;
	}
}
