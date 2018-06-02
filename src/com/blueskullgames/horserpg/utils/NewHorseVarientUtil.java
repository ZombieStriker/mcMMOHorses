package com.blueskullgames.horserpg.utils;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;

import com.blueskullgames.horserpg.RPGHorse;

@SuppressWarnings("deprecation")
public class NewHorseVarientUtil {

	public static boolean isHorse(EntityType e){

		if (e==EntityType.HORSE)
			return true;
		try {
			if (e==EntityType.DONKEY||e==EntityType.MULE)
				return true;
		} catch (Error e2) {
		}
		try {
			if(e==EntityType.ZOMBIE_HORSE||e==EntityType.SKELETON_HORSE||e==EntityType.LLAMA)
				return true;

		} catch (Error e2) {
		}
		return false;
	}

	public static EntityType getHorseType(String type) {
		if (type.contains("skele"))
			return EntityType.SKELETON_HORSE;
		if (type.contains("donkey"))
			return EntityType.DONKEY;
		if (type.contains("horse"))
			return EntityType.HORSE;
		if (type.contains("mule"))
			return EntityType.MULE;
		if (type.contains("zombie"))
			return EntityType.ZOMBIE_HORSE;
		if (type.contains("rand"))
			return getHorseByType(RPGHorse.randomVariant());
		if (type.contains("llama"))
			return EntityType.LLAMA;
		return EntityType.HORSE;
	}

	public static EntityType getHorseByType(Variant v) {
		if (v == Variant.HORSE)
			return EntityType.HORSE;
		try {
			if (v == Variant.DONKEY)
				return EntityType.DONKEY;
			if (v == Variant.MULE)
				return EntityType.MULE;
		} catch (Error e) {
		}
		try {
			if (v == Variant.SKELETON_HORSE)
				return EntityType.SKELETON_HORSE;
			if (v == Variant.UNDEAD_HORSE)
				return EntityType.ZOMBIE_HORSE;
			if (v == Variant.LLAMA)
				return EntityType.LLAMA;
		} catch (Error e) {
		}
		return EntityType.HORSE;
	}
}
