/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rafkind.masterofmagic.state

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
  val valuesAll = Array(CENTER, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
}

case class Plane(val id:Int, val name:String)
object Plane {
  val ARCANUS = Plane(0, "Arcanus")
  val MYRROR = Plane(1, "Myrror")

  val values = Array(ARCANUS, MYRROR)

  implicit def plane2string(p:Plane) = p.name
    
}

class Place {

}

class UnitStack {

}

class Player {
  // id
  // name
  // picture
  // color scheme
  // music scheme
}

// http://www.dragonsword.com/magic/eljay/SaveGam.html

case class TerrainType(val id:Int, val name:String)
object TerrainType {
  val OCEAN = TerrainType(0, "Ocean");
  val SHORE = TerrainType(1, "Shore");
  val RIVER = TerrainType(2, "River");
  val SWAMP = TerrainType(3, "Swamp");
  val TUNDRA = TerrainType(4, "Tundra");
  val DEEP_TUNDRA = TerrainType(5, "Deep Tundra");
  val MOUNTAIN = TerrainType(6, "Mountain");
  val VOLCANO = TerrainType(7, "Volcano");
  val CHAOS_NODE = TerrainType(8, "Chaos Node");
  val HILLS = TerrainType(9, "Hills");
  val GRASSLAND = TerrainType(10, "Grassland");
  val SORCERY_NODE = TerrainType(11, "Sorcery Node");
  val DESERT = TerrainType(12, "Desert");
  val FOREST = TerrainType(13, "Forest");
  val NATURE_NODE = TerrainType(14, "Nature Node");

  val values = Array(
    OCEAN,
    SHORE,
    RIVER,
    SWAMP,
    TUNDRA,
    DEEP_TUNDRA,
    MOUNTAIN,
    VOLCANO,
    CHAOS_NODE,
    HILLS,
    GRASSLAND,
    SORCERY_NODE,
    DESERT,
    FOREST,
    NATURE_NODE);

  implicit def terrainType2string(t:TerrainType) = t.name
}

case class TerrainTileMetadata(
  val id:Int,
  val terrainType:TerrainType,
  val borderingTerrainTypes:Array[Option[TerrainType]],
  val plane:Plane,
  val parentId:Option[TerrainTileMetadata])

object TerrainTileMetadata {
  import com.rafkind.masterofmagic.util.TerrainLbxReader._;
  
  var data:Array[TerrainTileMetadata] = 
    new Array[TerrainTileMetadata](TILE_COUNT);

  def read(fn:String):Unit = {
    
  }
}

object TerrainSquare {
  
}

class TerrainSquare(
  var spriteNumber:Int,
  var terrainType:TerrainType,
  var fogOfWarBitset:Int,
  var pollutionFlag:Boolean,
  var roadBitset:Int,
  var building:Option[Place],
  var unitStack:Option[UnitStack]) {
    
  // what type of terrain
  // what terrain tile to use
  // bitset for fog of war
  // polluted?
  // what type of bonus
  // road?
  // what city is here?
  // what unit stack is here?
}


object Overworld {
  val WIDTH = 60;
  val HEIGHT = 40;

  

  /*
   * Arcanus:
   * OCEAN = 0
   * SHORE = XXX
   * GRASSLAND = 1
   * FOREST = 274
   * MOUNTAIN = 275
   * DESERT = 276
   * SWAMP = 277
   * TUNDRA = 278
   * DEEP_TUNDRA = XXX
   * SORCERY NODE = 279
   * NATURE NODE = 283
   * CHAOS NODE = 287
   * HILLS = 291
   * VOLCANO = 299
   * RIVER = 591
   *
   * Myrror:
   * OCEAN = 888
   * GRASSLAND = 889
   * FOREST = 1147
   * MOUNTAIN = 1148
   * DESERT = 1149
   * SWAMP = 1150
   * SORCERY NODE = 1152
   * FOREST NODE = 1186
   * CHAOS NODE = 1160
   * HILLS = 1164
   * VOLCANO = 1172
   * RIVER = 1464
   * TUNDRA = 1605
   */

  def create():Overworld = {

    val arcanus = Map(TerrainType.OCEAN -> 0,
      TerrainType.GRASSLAND -> 1,
      TerrainType.FOREST -> 274,
      TerrainType.MOUNTAIN -> 275,
      TerrainType.DESERT -> 276,
      TerrainType.SWAMP -> 277,
      TerrainType.TUNDRA -> 278,
      TerrainType.SORCERY_NODE -> 279,
      TerrainType.NATURE_NODE -> 283,
      TerrainType.CHAOS_NODE -> 287,
      TerrainType.HILLS -> 291,
      TerrainType.VOLCANO -> 299,
      TerrainType.RIVER -> 591);
    val myrror = Map(TerrainType.OCEAN -> 888);

    var overworld = new Overworld(WIDTH, HEIGHT);
    
    overworld.fillPlane(Plane.ARCANUS, arcanus(TerrainType.OCEAN), TerrainType.OCEAN);
    overworld.fillPlane(Plane.MYRROR, myrror(TerrainType.OCEAN), TerrainType.OCEAN);

    overworld.buildPlane(Plane.ARCANUS, 5, 800);

    for (y <- 0 until HEIGHT) {
      for (x <- 0 until WIDTH) {
        var t = overworld.get(Plane.ARCANUS, x, y);
        t.spriteNumber = arcanus(t.terrainType);
      }
    }


    /*overworld.buildPlane(Plane.MYRROR, 3, 10);
    for (y <- 0 until HEIGHT) {
      for (x <- 0 until WIDTH) {
        var t = overworld.get(Plane.MYRROR, x, y);
        t.spriteNumber = myrror(t.terrainType);
      }
    }*/

    return overworld;
  }
}

class Overworld(val width:Int, val height:Int) {
  import scala.util.Random;
  
  var terrain:Array[TerrainSquare] = 
    new Array[TerrainSquare](2 * width * height);

  def get(plane:Plane, x:Int, y:Int):TerrainSquare = {
    val xx = x % width;

    if (y >= 0 && y <= height) {
      return terrain(plane.id * width * height
                     + y * width
                     + xx);
    } else {
      throw new IllegalArgumentException("Bad coordinates");
    }
  }

  def put(plane:Plane, x:Int, y:Int, terrainSquare:TerrainSquare):Unit = {
    val xx = x % width;
    terrain(plane.id * width * height
            + y * width
            + xx) = terrainSquare;
  }

  def fillPlane(plane:Plane, tileId:Int, terrain:TerrainType):Unit = {
    for (y <- 0 until height) {
      for (x <- 0 until width) {
        put(plane, x, y,
                      new TerrainSquare(
                        tileId,
                        terrain,
                        0,
                        false,
                        0,
                        None,
                        None));
      }
    }
  }

  def findWhereTrue(random:Random,
                    plane:Plane,
                    predicate:(Int,Int) => Boolean):Tuple2[Int,Int] = {

    var x:Int = 0;
    var y:Int = 0;
    do {
      x = random.nextInt(width);
      y = random.nextInt(height);
    } while (!predicate(x,y));

    return (x,y);
  }

  def grow(random:Random,
           plane:Plane,
           numSeeds:Int,
           groundCount:Int,
           base:TerrainType,
           topping:TerrainType):Unit = {
    for(n <- 0 until numSeeds) {
      findWhereTrue(random, 
                     plane,
                     (x,y) => {
                        get(plane, x, y).terrainType == base
                      }) match {
        case (x,y) => {
          var t = get(plane, x, y);
          t.terrainType = topping;
        }                
      }
    }

    for (n <- 0 until groundCount) {
      findWhereTrue(random,
                     plane,
                     (x,y) => {
                        var found = false;
                        if (get(plane, x, y).terrainType == base) {
                          for (dir <- CardinalDirection.valuesStraight) {
                            val nx = x + dir.dx + width;
                            val ny = y + dir.dy;
                            if (ny >=0 && ny < height) {
                              if (get(plane, nx, ny).terrainType == topping) {
                                found = true;
                              }
                            }
                          }
                        }
                        found;
                      }) match {
        case (x,y) => {
          var t = get(plane, x, y);
          t.terrainType = topping;
        }
      }
    }

    // fill in the gaps
    for (y <- 0 to height) {
      for (x <- 0 to width) {
        if (get(plane, x, y).terrainType == base) {
          var count = 0;
          for (dir <- CardinalDirection.valuesStraight) {
            val nx = x + dir.dx + width;
            val ny = y + dir.dy;
            if (ny >=0 && ny < height) {
              if (get(plane, nx, ny).terrainType == topping) {
                count += 1;
              }
            }
          }
          if (count == 4) {
            var t = get(plane, x, y);
            t.terrainType = topping;
          }
        }
      }
    }
  }

  def buildPlane(plane:Plane, numSeeds:Int, groundCount:Int):Unit = {
    var random = new Random();

    grow(random, plane, numSeeds, groundCount, TerrainType.OCEAN, TerrainType.GRASSLAND);
    grow(random, plane, numSeeds * 2, groundCount / 10, TerrainType.GRASSLAND, TerrainType.FOREST);
    grow(random, plane, numSeeds * 2, groundCount / 10, TerrainType.GRASSLAND, TerrainType.HILLS);
    grow(random, plane, numSeeds * 2, groundCount / 10, TerrainType.GRASSLAND, TerrainType.MOUNTAIN);
    grow(random, plane, numSeeds * 2, groundCount / 10, TerrainType.GRASSLAND, TerrainType.DESERT);
    grow(random, plane, numSeeds * 2, groundCount / 10, TerrainType.GRASSLAND, TerrainType.SWAMP);

    grow(random, plane, numSeeds, 0, TerrainType.GRASSLAND, TerrainType.SORCERY_NODE);
    grow(random, plane, numSeeds, 0, TerrainType.MOUNTAIN, TerrainType.CHAOS_NODE);
    grow(random, plane, numSeeds, 0, TerrainType.FOREST, TerrainType.NATURE_NODE);
    
    // north and south poles, and tundra
    for (x <- 0 until width) {
      get(plane, x, 0).terrainType = TerrainType.TUNDRA;
      get(plane, x, height-1).terrainType = TerrainType.TUNDRA;
      for (y <- 0 until height / 8) {
        if (get(plane, x, y).terrainType == TerrainType.GRASSLAND ||
          get(plane, x, y).terrainType == TerrainType.HILLS ||
          get(plane, x, y).terrainType == TerrainType.DESERT) {
          get(plane, x, y).terrainType = TerrainType.TUNDRA;
        }
        val yy = height - (y+1);
        if (get(plane, x, yy).terrainType == TerrainType.GRASSLAND ||
          get(plane, x, yy).terrainType == TerrainType.HILLS ||
          get(plane, x, yy).terrainType == TerrainType.DESERT) {
          get(plane, x, yy).terrainType = TerrainType.TUNDRA;
        }
      }
    }


  }

}

class State {
  // players
  // normal world, mirror world
  // cities
  // lairs
  // magic nodes
  // unit stacks
  // units
}
