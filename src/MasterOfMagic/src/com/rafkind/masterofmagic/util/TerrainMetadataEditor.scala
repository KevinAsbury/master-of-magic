/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rafkind.masterofmagic.util

import org.newdawn.slick._;

import com.rafkind.masterofmagic.system._;
import com.rafkind.masterofmagic.state._;
import scala.xml.XML._;
import scala.xml._;

// mirrors TerrainTileMetadata in State.scala
class EditableTerrainTileMetadata(
  var id:Int,
  var terrainType:TerrainType,
  var borderingTerrainTypes:Array[Option[TerrainType]],
  var plane:Plane,
  var parentId:Option[TerrainTileMetadata]) {

  def toNode() =
    <metadata 
      id={id.toString}
      terrainType={terrainType.id.toString}
      plane={plane.id.toString}>
      {(borderingTerrainTypes zip CardinalDirection.values) map {
        case (b, d) =>
          b match {
            case Some(x) => <borders direction={d.id.toString} terrain={x.id.toString} />
            case _ =>
          }
      }
    }
    </metadata>
}

class TerrainMetadataEditor(title:String) extends BasicGame(title) {
  import com.rafkind.masterofmagic.util.TerrainLbxReader._;
  
  var terrainTileSheet:Image = null;

  var currentTile:Int = 0;

  var currentDirection:CardinalDirection = CardinalDirection.CENTER;

  val BIG_WIDTH = 133;
  val BIG_HEIGHT = BIG_WIDTH * TILE_HEIGHT / TILE_WIDTH;

  val uiColor = Color.white;

  var metadata = new Array[EditableTerrainTileMetadata](TILE_COUNT);

  load(Data.path("terrainMetaData.xml")) \ "metadata" foreach { (m) =>
    var borders = new Array[Option[TerrainType]](CardinalDirection.values.length);
    m \ "borders" foreach { (b) =>
      borders(Integer.parseInt((b \ "@direction").text)) =
        Some(TerrainType.values(Integer.parseInt((b \ "@terrain").text)));
    }

    val id = Integer.parseInt((m \ "@id").text)
    val terrainType = Integer.parseInt((m \ "@terrainType").text);
    val plane = Integer.parseInt((m \ "@plane").text);
    metadata(id) = new EditableTerrainTileMetadata(id,
                                            TerrainType.values(terrainType),
                                            borders,
                                            Plane.values(plane), None);
  }

  /*for (i <- 0 until metadata.length) {

    var borders = new Array[Option[TerrainType]](CardinalDirection.values.length);
    for (j <- 0 until borders.length) {
      borders(j) = None;
    }
    metadata(i) = new EditableTerrainTileMetadata(i,
                                          TerrainType.OCEAN,
                                          borders,
                                          Plane.ARCANUS, None);
  }*/


  override def init(container:GameContainer):Unit = {
    terrainTileSheet = TerrainLbxReader.read(Data.originalDataPath("TERRAIN.LBX"));
    org.lwjgl.input.Keyboard.enableRepeatEvents(false);
  }

  override def update(container:GameContainer, delta:Int):Unit = {
    val input = container.getInput();

    val keys = Array(
      Input.KEY_K,
      Input.KEY_I,
      Input.KEY_O,
      Input.KEY_L,
      Input.KEY_PERIOD,
      Input.KEY_COMMA,
      Input.KEY_M,
      Input.KEY_J,
      Input.KEY_U);
    
    (keys zip CardinalDirection.valuesAll) map {
      case (k, d) =>
        if (input.isKeyPressed(k)) {
          currentDirection = d;
          input.clearKeyPressedRecord();
        }
    }

    if (input.isKeyPressed(Input.KEY_RBRACKET)) {
      if (currentTile < TILE_COUNT) {
        currentTile += 1;
      }
      input.clearKeyPressedRecord();
    }

    if (input.isKeyPressed(Input.KEY_LBRACKET)) {
      if (currentTile > 0) {
        currentTile -= 1;
      }
      input.clearKeyPressedRecord();
    }

    val terrainKeys = Array(
      Input.KEY_Q,
      Input.KEY_W,
      Input.KEY_E,
      Input.KEY_R,
      Input.KEY_T,
      Input.KEY_A,
      Input.KEY_S,
      Input.KEY_D,
      Input.KEY_F,
      Input.KEY_G,
      Input.KEY_Z,
      Input.KEY_X,
      Input.KEY_C,
      Input.KEY_V,
      Input.KEY_B
    );

    (terrainKeys zip TerrainType.values) map {
      case (k, t) =>
        if (input.isKeyPressed(k)) {
          if (currentDirection == CardinalDirection.CENTER) {
            metadata(currentTile).terrainType = t
          } else {
            metadata(currentTile).borderingTerrainTypes(currentDirection.id) = Some(t);
          }
          input.clearKeyPressedRecord();
        }
    }

    if (input.isKeyPressed(Input.KEY_P)) {
          metadata(currentTile).plane match {
            case Plane.ARCANUS => metadata(currentTile).plane = Plane.MYRROR;
            case Plane.MYRROR => metadata(currentTile).plane = Plane.ARCANUS;
          }

          input.clearKeyPressedRecord();
        }

    if (input.isKeyPressed(Input.KEY_ESCAPE)) {
      writeOut();
      System.exit(0);
    }
  }

  def writeOut() {
    
    val doc =
      <terrain>
        {metadata.map(m =>{m.toNode()})}
      </terrain>;

    save(Data.path("terrainMetaData.xml"), doc, "utf-8", true);
  }

  override def render(container:GameContainer, graphics:Graphics):Unit = {

    val tX = (currentTile % SPRITE_SHEET_WIDTH) * TILE_WIDTH;
    val tY = (currentTile / SPRITE_SHEET_WIDTH) * TILE_HEIGHT;

    val dX = (container.getWidth()-BIG_WIDTH)/2;
    val dY = (container.getHeight()-BIG_HEIGHT)/2;

    terrainTileSheet.draw(
      dX, dY, dX + BIG_WIDTH, dY + BIG_HEIGHT,
      tX, tY, tX + TILE_WIDTH, tY + TILE_HEIGHT
    );

    // draw direction box
    val cX = container.getWidth()/2;
    val cY = container.getHeight()/2;
    
    val x1 = cX + BIG_WIDTH * currentDirection.dx - (10 + BIG_WIDTH/2);
    val y1 = cY + BIG_HEIGHT * currentDirection.dy - (10 + BIG_HEIGHT / 2);
    
    val x2 = cX + BIG_WIDTH * currentDirection.dx + (10 + BIG_WIDTH/2);
    val y2 = cY + BIG_HEIGHT * currentDirection.dy + (10 + BIG_HEIGHT / 2);

    graphics.setColor(uiColor);
    graphics.drawRect(x1, y1, x2-x1, y2-y1);

    graphics.drawString(
      metadata(currentTile).plane,
      dX,
      dY);

    graphics.drawString(
      metadata(currentTile).terrainType,
      dX,
      dY + 16
      );

    graphics.drawString(
      "Tile #" + currentTile,
      dX,
      dY + 32
      );

    (metadata(currentTile).borderingTerrainTypes zip CardinalDirection.values) map {
      case (optionalTerrain, direction) =>
        graphics.drawString(optionalTerrain match {
            case Some(terrain) => terrain
            case _ => ""
          },
                            cX + BIG_WIDTH * direction.dx - BIG_WIDTH/2,
                            cY + BIG_HEIGHT * direction.dy - BIG_HEIGHT / 2)
    }    
  }
}

object TerrainMetadataEditor {
  def main(args: Array[String]): Unit = {
    var app = new AppGameContainer(new TerrainMetadataEditor("Master of Magic: Terrain Metadata Editor"));
    org.lwjgl.input.Keyboard.enableRepeatEvents(true);
    app.setDisplayMode(640, 400, false);
    app.setSmoothDeltas(true);
    app.setTargetFrameRate(40);
    app.setShowFPS(false);
    app.start();
  }
}