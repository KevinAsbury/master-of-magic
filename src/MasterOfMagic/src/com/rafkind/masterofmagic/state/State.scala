/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rafkind.masterofmagic.state


import scala.util.Random;

case class MagicColor(val id:Int, val name:String)
object MagicColor {
  val ARCANE  = MagicColor(0, "Arcane");
  val WHITE = MagicColor(1, "White");
  val GREEN = MagicColor(2, "Green");
  val RED = MagicColor(3, "Red");
  val BLUE = MagicColor(4, "Blue");
  val BLACK = MagicColor(5, "Black");
}

case class CardinalDirection(val id:Int, val dx:Int, val dy:Int)
object CardinalDirection{

  val NORTH       = CardinalDirection(0, 0, -1);
  val NORTH_EAST  = CardinalDirection(1, 1, -1);
  val EAST        = CardinalDirection(2, 1, 0);
  val SOUTH_EAST  = CardinalDirection(3, 1, 1);
  val SOUTH       = CardinalDirection(4, 0, 1);
  val SOUTH_WEST  = CardinalDirection(5, -1, 1);
  val WEST        = CardinalDirection(6, -1, 0);
  val NORTH_WEST  = CardinalDirection(7, -1, -1);
  val CENTER      = CardinalDirection(8, 0, 0);

  val values = Array(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
  val valuesStraight = Array(NORTH, EAST, SOUTH, WEST);
  val valuesDiagonal = Array(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
  val valuesAll = Array(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, CENTER);
}

case class Plane(val id:Int, val name:String)
object Plane {
  val ARCANUS = Plane(0, "Arcanus")
  val MYRROR = Plane(1, "Myrror")

  val values = Array(ARCANUS, MYRROR)

  implicit def plane2string(p:Plane) = p.name  
}

case class Race(val id:Int, val name:String)
object Race {
  val BARBARIAN = Race(0, "Barbarian");
  val GNOLL = Race(1, "Gnoll");
  val HALFLING = Race(2, "Halfling");
  val HIGH_ELF = Race(3, "High Elf");
  val HIGH_MEN = Race(4, "High Man");
  val KLACKON = Race(5, "Klackon");
  val LIZARDMAN = Race(6, "Lizardman");
  val NOMAD = Race(7, "Nomad");
  val ORC = Race(8, "Orc");
  val BEASTMAN = Race(9, "Beastman");
  val DARK_ELF = Race(10, "Dark Elf");
  val DRACONIAN = Race(11, "Draconian");
  val DWARVEN = Race(12, "Dwarven");
  val TROLL = Race(13, "Troll");

  val values = Array(BARBARIAN,
                     GNOLL,
                     HALFLING,
                     HIGH_ELF,
                     HIGH_MEN,
                     KLACKON,
                     LIZARDMAN,
                     NOMAD,
                     ORC,
                     BEASTMAN,
                     DARK_ELF,
                     DRACONIAN,
                     DWARVEN,
                     TROLL);

  val valuesByPlane = Array(
    Array(BARBARIAN,
         GNOLL,
         HALFLING,
         HIGH_ELF,
         HIGH_MEN,
         KLACKON,
         LIZARDMAN,
         NOMAD,
         ORC),
    Array(BEASTMAN,
         DARK_ELF,
         DRACONIAN,
         DWARVEN,
         TROLL));

  implicit def race2string(r:Race) = r.name;
}

class Spell {

}

class Item {

}

class Retort {
  
}

case class LairType(val id:Int, val name:String)
object LairType {
  val CAVE = LairType(0, "Cave");
  val TEMPLE = LairType(1, "Temple");
  val KEEP = LairType(2, "Keep");
  val TOWER = LairType(3, "Tower");
  val NODE = LairType(4, "Node");
  
  val values = Array(CAVE, TEMPLE, KEEP);

  def getRandom(random:Random):LairType =
    values(random.nextInt(values.length));
}

class Place(val x:Int, val y:Int) {

}

object City {
  def createNeutralCity(
                 owner:Player,
                 x:Int,
                 y:Int,
                 name:String,
                 race:Race):City = {
    return new City(x, y, owner, name, race);
  }
}
class City(x:Int, y:Int, val owner:Player, val name:String, val race:Race) extends Place(x, y) {
}

object LairReward {
  
  // powerlevel from 0 to 10
  def createReward(random:Random, powerLevel:Int):LairReward = {
    return new LairReward(10, 0, 0, 0, 0, 0, 0, 0);
  }
}
class LairReward(
  val gold:Int,
  val mana:Int,
  val spellLevel:Int,
  val spellBooksCount:Int,
  val prisonerHeroLevel:Int,
  val itemLevel:Int,
  val itemCount:Int,
  val retortCount:Int
  ) {

}

object Lair {
  def createLair(random:Random, 
                 x:Int,
                 y:Int,
                 lairType:LairType,
                 powerLevel:Int):Lair = {
    val nativeUnits = new ArmyUnitStack;
    val reward = LairReward.createReward(random, powerLevel);
    return new Lair(lairType, nativeUnits, reward, x, y);
  }
}
class Lair(val lairType:LairType,
           val nativeUnits:ArmyUnitStack,
           val reward:LairReward,
           x:Int,
           y:Int) extends Place(x, y) {

}

object Node {
  def createNode(random:Random, x:Int, y:Int, nodeType:TerrainType, powerLevel:Int):Node = {
    val nativeUnits = new ArmyUnitStack;
    val reward = LairReward.createReward(random, powerLevel);
    return new Node(nodeType, nativeUnits, reward, x, y);
  }
}
class Node(val nodeType:TerrainType,
           nativeUnits:ArmyUnitStack,
           reward:LairReward,
           x:Int,
           y:Int) extends Lair(LairType.NODE, nativeUnits, reward, x, y) {
  
}

class ArmyUnit {

}

class Hero extends ArmyUnit {

}

class ArmyUnitStack {

}

class Player(val name:String) {
  // id
  // name
  // picture
  // color scheme
  // music scheme

  var armyUnits:List[ArmyUnit] = List();

  var armyUnitStacks:List[ArmyUnitStack] = List();
  
  var cities:List[City] = List();
}

// http://www.dragonsword.com/magic/eljay/SaveGam.html

object State {
  def createGameState(numberOfPlayers:Int):State = {
    val state = new State(numberOfPlayers);

    state;
  }
}

class State(numberOfPlayers:Int) {
  // players
  val players = new Array[Player](numberOfPlayers+1);
  players(0) = new Player("Raiders");

  val _overworld = Overworld.create(players(0));

  def overworld = _overworld;
}