package com.naus3a.cyborgcompanion;

import java.util.function.Predicate;

public class PasswordBuilder {
	
	enum PasswordFeature{
		LOWERCASE,
		UPPERCASE,
		DIGIT,
		SYMBOL
	}
	
	private long[] numFeatures = new long[]{0,0,0,0};
	private String password = "";
	
	public PasswordBuilder(String s){
		checkFeatures(s);
		if(isGoodPassword()){
			password = s;
		}else {
			String temp = s;
			for(PasswordFeature pf : PasswordFeature.values()){
				temp = injectFeatureIfNeeded(temp, pf);
			}
			password = temp;
		}
	}
	
	public String getPassword(){return password;}
	
	private String injectFeatureIfNeeded(String s, PasswordFeature pf){
		if(getNumFeature(pf)>0)return s;
		
		switch (pf){
			case LOWERCASE:
				if(getNumFeature(PasswordFeature.UPPERCASE)>1){
					int idx = findFirstUpperCase(s);
					Character c = Character.toLowerCase( s.charAt(idx));
					return inject(s, idx, c);
				}else{
					return injectInFirstMultiEntryFeature(s, 'a');
				}
			case UPPERCASE:
				if(getNumFeature(PasswordFeature.LOWERCASE)>1){
					int idx = findFirstLowerCase(s);
					Character c = Character.toUpperCase(s.charAt(idx));
					return inject(s,idx, c);
				}else {
					return injectInFirstMultiEntryFeature(s, 'A');
				}
			case DIGIT:
				return injectInFirstMultiEntryFeature(s, '0');
			case SYMBOL:
				return injectInFirstMultiEntryFeature(s, '-');
			default :
				return "";
		}
	}
	
	private String inject(String s, int idx, Character c){
		StringBuilder sb = new StringBuilder(s);
		sb.setCharAt(idx, c);
		return sb.toString();
	}
	
	private String injectInFirstMultiEntryFeature(String s, Character c){
		PasswordFeature otherFeature = getFirstMultiEntryFeature();
		int idx = findFirst(otherFeature, s);
		return inject(s, idx, c);
	}
	
	private PasswordFeature getFirstMultiEntryFeature(){
		for(int i=0;i<numFeatures.length;i++){
			if(numFeatures[i]>1) return PasswordFeature.values()[i];
		}
		return PasswordFeature.LOWERCASE;
	}
	
	private boolean isGoodPassword(){
		return getNumFeature(PasswordFeature.LOWERCASE)>0 &&
				getNumFeature(PasswordFeature.UPPERCASE)>0 &&
				getNumFeature(PasswordFeature.DIGIT)>0 &&
				getNumFeature(PasswordFeature.SYMBOL)>0;
	}
	
	private void setNumFeature(PasswordFeature pf, long n){
		numFeatures[pf.ordinal()] = n;
	}
	
	private long getNumFeature(PasswordFeature pf){
		return numFeatures[pf.ordinal()];
	}
	
	private void resetNumFeatures(){
		for(int i=0;i<numFeatures.length;i++) numFeatures[i] = 0;
	}
	
	private void checkFeatures(String s){
		resetNumFeatures();
		setNumFeature(PasswordFeature.LOWERCASE, countLowerCase(s));
		setNumFeature(PasswordFeature.UPPERCASE, countUpperCase(s));
		setNumFeature(PasswordFeature.DIGIT, countDigit(s));
		long numNonSymbol = getNumFeature(PasswordFeature.LOWERCASE)+getNumFeature(PasswordFeature.UPPERCASE)+getNumFeature(PasswordFeature.DIGIT);
		setNumFeature(PasswordFeature.SYMBOL, s.length()-numNonSymbol);
	}
	
	private long countFeatures(String s, Predicate<Character> condition){
		return s.chars()
		.filter(c -> condition.test((char)c))
		.count();
	}
	
	private int findFirstFeature(String s, Predicate<Character> condition){
		for(int i=0;i<s.length();i++){
			Character c = s.charAt(i);
			if(condition.test(c)) return i;
		}
		return -1;
	}
	
	private int findFirstLowerCase(String s){return findFirstFeature(s, Character::isLowerCase);}
	private int findFirstUpperCase(String s){return findFirstFeature(s, Character::isUpperCase);}
	private int findFirstDigit(String s){return findFirstFeature(s, Character::isDigit);}
	
	private int findFirstSymbol(String s){
		Predicate<Character> alpha = Character::isAlphabetic;
		Predicate<Character> digit = Character::isDigit;
		Predicate<Character> nonsy = alpha.or(digit);
		Predicate<Character> condition = nonsy.negate();
		
		return findFirstFeature(s, condition);
	}
	
	private int findFirst(PasswordFeature pf, String s){
		switch (pf){
			case LOWERCASE:
				return findFirstLowerCase(s);
			case UPPERCASE:
				return findFirstUpperCase(s);
			case DIGIT:
				return findFirstDigit(s);
			case SYMBOL:
				return findFirstSymbol(s);
			default :
				return -1;
		}
	}
	
	private long countLowerCase(String s){
		return countFeatures(s, Character::isLowerCase);
	}
	
	private long countUpperCase(String s){
		return countFeatures(s, Character::isUpperCase);
	}
	
	private long countDigit(String s){
		return countFeatures(s, Character::isDigit);
	}
}