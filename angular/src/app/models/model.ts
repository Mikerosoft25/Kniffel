import { Timestamp } from 'rxjs';

export interface Game {
  id: string;
  players: Player[];
  dices: Dice[];
  status: GameStatus;
  playerTurn?: number;
  rollCount?: number;
  round?: number;
}

export interface Player {
  id: string;
  name: string;
  host: boolean;
  connected: boolean;
  points: number[];
}

export interface Dice {
  value: number;
  index: number;
  locked: boolean;
}

export interface TableCell {
  row: number;
  column: number;
}

export enum GameStatus {
  CREATED = 'CREATED',
  STARTED = 'STARTED',
  ENDED = 'ENDED',
}

export interface ErrorDTO {
  timestamp: string;
  status: string;
  statusCode: number;
  message: string;
  path: string;
}

export interface WebSocketRequest<T> {
  requesterUUID: string;
  payload?: T;
}

export interface WebSocketResponse {
  messageType: WebSocketResponseMessageType;
  status: WebSocketResponseStatus;
  targetUUID?: string;
  game?: Game;
}

export enum WebSocketResponseStatus {
  OK = 'OK',
  INVALID_REQUEST = 'PLAYER_HAS_NOT_ROLLED',
  GAME_NOT_FOUND = 'GAME_NOT_FOUND',
  PLAYER_NOT_FOUND = 'PLAYER_NOT_FOUND',
  PLAYER_IS_NOT_HOST = 'PLAYER_IS_NOT_HOST',
  PLAYER_IS_NOT_AT_TURN = 'PLAYER_IS_NOT_AT_TURN',
  PLAYER_HAS_NOT_ROLLED = 'PLAYER_HAS_NOT_ROLLED',
  WRONG_TABLE_COLUMN = 'WRONG_TABLE_COLUMN',
  TABLE_CELL_NOT_EMPTY = 'TABLE_CELL_NOT_EMPTY',
  NO_MORE_ROLL = 'NO_MORE_ROLL',
}

export enum WebSocketResponseMessageType {
  ERROR = 'ERROR',
  PLAYER_CONNECTED = 'PLAYER_CONNECTED',
  PLAYER_KICKED = 'PLAYER_KICKED',
  GAME_STARTED = 'GAME_STARTED',
  PLAYER_IS_NOW_HOST = 'PLAYER_IS_NOW_HOST',
}
