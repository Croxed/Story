package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.entity.Entity;

import org.newdawn.slick.util.Log;

public class CooldownManager {

	// Create Map to store Key-Multiple Values (a.k.a Cooldown(s))
	static Map<Entity, ArrayList<Cooldown>> hashMap = new HashMap<Entity, ArrayList<Cooldown>>();

	public static class Cooldown
	{
		// Time on the cooldown in milliseconds
		int time;

		// Time passed since the start of cooldown
		int timePassed;

		// Name of the cooldown - fireBall, hurtCD, etc
		String name;

		// Reference to the Entity which this cooldown belongs to
		Entity ownerEntity;

		// Indicates if the cooldown is in effect or not
		boolean activated = false;

		public Cooldown(String name, int time, Entity ownerEntity)
		{
			this.name = name;
			this.time = time;
			this.ownerEntity = ownerEntity;
		}

		/**
		 * Updates the cooldowns' time
		 * @param delta
		 */
		public void update(int delta)
		{
			timePassed += delta;
			if(timePassed >= time)
			{
				finish();
			}
		}

		/**
		 * Finishing the cooldown
		 */
		public void finish()
		{
			callOwnerEntity();
			deActivate();
			refresh();
		}

		/**
		 * Notifies the owner of the cooldown
		 */
		public void callOwnerEntity()
		{
			ownerEntity.cooldownFinished(name);
		}

		/**
		 * Restores the remaining time to the orginal time on the cooldown
		 */
		public void refresh()
		{
			timePassed = 0;
		}

		/**
		 * Remaining time of the cooldown
		 * @return Time remaining on the cooldown in milliseconds
		 */
		public int getTimeRemaining()
		{
			return time - timePassed;
		}

		/**
		 * The name of the cooldown
		 * @return Name of the cooldown
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Checks if the cooldown is in effect
		 * @return
		 */
		public boolean isActivated()
		{
			return activated;
		}

		/**
		 * Deactivated the cooldown
		 */
		public void deActivate()
		{
			activated = false;
		}

		/**
		 * Activates the cooldown
		 */
		public void activate()
		{
			activated = true;
		}

		/**
		 * Sets the time for the cooldown (lenght/runtime ... )
		 */
		public void setTime(int time)
		{
			this.time = time;
		}
	}

	/**
	 * RepeatingCooldown is useful for periodically effects & other things that happen more than once
	 * or on a fixed interval.
	 */
	public static class RepeatingCooldown extends Cooldown
	{
		// Number of times this cooldown shall repeat
		int repeat;

		// Number of times this cooldown have repeated
		int counter;

		public RepeatingCooldown(String name, int time, Entity ownerEntity, int repeat) 
		{
			super(name, time, ownerEntity);
			this.repeat = repeat;
		}

		@Override
		public void update(int delta)
		{
			timePassed += delta;
			if(timePassed >= time)
			{
				counter++;
				callOwnerEntity();
				timePassed = 0;
				if(counter >= repeat)
				{
					finish();
				}
			}
		}

		@Override
		public void refresh()
		{
			timePassed = 0;
			counter = 0;
		}

		public void setRepeat(int repeat)
		{
			this.repeat = repeat;
		}
	}

	public CooldownManager()
	{
		Log.info("CooldownManager initialized ... ");
	}

	/**
	 * Updates all the cooldowns in the HashMap by passing time passed since last frame
	 * @param delta
	 */
	public void update(int delta)
	{
		for(ArrayList<Cooldown> cooldownArrayList : hashMap.values())
		{
			for(Cooldown cooldown : cooldownArrayList)
			{
				if(cooldown.activated)
				{
					cooldown.update(delta);
				}
			}
		}
	}

	/**
	 * Sets a new cooldown on the specific Entity
	 * @args name of the cooldown, time of the cooldown, ownerEntity of the cooldown
	 */
	public void registerNewCooldown(String name, int time, Entity ownerEntity)
	{
		hashMap.get(ownerEntity).add(new Cooldown(name, time, ownerEntity));
	}

	/**
	 * Registers a new repeating cooldown on the specific Entity
	 * @param name Name of the cooldown
	 * @param time The length of the cooldown in milliseconds
	 * @param ownerEntity The Entity that this cooldown belongs to
	 * @param repeat Number of times to repeat this cooldown.
	 */
	public void registerNewRepeatingCooldown(String name, int time, Entity ownerEntity, int repeat)
	{
		hashMap.get(ownerEntity).add(new RepeatingCooldown(name, time, ownerEntity, repeat));
	}

	/**
	 * Registers a new Entity in the internal HashMap - if Entity wants to use cooldowns it needs to register.
	 */
	public void registerNewEntity(Entity ownerEntity)
	{
		hashMap.put(ownerEntity, new ArrayList<Cooldown>());
	}

	/**
	 * Ends the cooldown, does NOT send a cooldownFinished signal
	 */
	public void deactivateCooldown(Entity entity, String cooldownName)
	{
		for(Cooldown cooldown : hashMap.get(entity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				cooldown.deActivate();
			}
		}
	}

	/**
	 * Activates (starts) the cooldown also sets the time of the cooldown. 
	 * It essentially makes sure the cooldown is activated and goes for the time passed on.
	 */
	public void activateCooldown(Entity ownerEntity, String cooldownName, int time)
	{
		for(Cooldown cooldown : hashMap.get(ownerEntity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				cooldown.activate();
				cooldown.setTime(time);
			}
		}
	}

	/**
	 * Activates (starts) the repeating cooldown also sets the time of the cooldown & repeat.
	 * It essentially makes sure the cooldown is activated and goes for the time passed and the repeat passed.
	 */
	public void activateCooldown(Entity ownerEntity, String cooldownName, int time, int repeat)
	{
		for(Cooldown cooldown : hashMap.get(ownerEntity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				cooldown.activate();
				cooldown.setTime(time);
				((RepeatingCooldown) cooldown).setRepeat(repeat);
			}
		}
	}

	/**
	 * Ends the cooldown and sends a cooldownFinished signal
	 */
	public void finishCooldownPrematurely(Entity entity, String cooldownName)
	{
		for(Cooldown cooldown : hashMap.get(entity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				cooldown.finish();
			}
		}
	}

	/**
	 * Gets the remaining time on a specific cooldown
	 * @return Time remaining for the specific cooldown or 0 if it does not exist.
	 */
	public int getRemainderTime(Entity entity, String cooldownName)
	{
		for(Cooldown cooldown : hashMap.get(entity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				return cooldown.getTimeRemaining();
			}
		}
		return 0;
	}

	/**
	 * Restores the original time on the specific cooldown
	 */
	public void refreshCooldown(Entity entity, String cooldownName)
	{
		for(Cooldown cooldown : hashMap.get(entity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				cooldown.refresh();
			}
		}
	}


	/**
	 * Change the time of a specific cooldown
	 */
	public void changeTime(Entity ownerEntity, String cooldownName, int time)
	{
		for(Cooldown cooldown : hashMap.get(ownerEntity))
		{
			if(cooldown.getName().equals(cooldownName))
			{
				cooldown.setTime(time);
			}
		}
	}
}