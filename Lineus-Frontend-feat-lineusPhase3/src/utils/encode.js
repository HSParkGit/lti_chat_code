import { Buffer } from 'buffer';

export function encodeArrayToBase64(array) {
  return Buffer.from(JSON.stringify(array)).toString('base64');
}

export function decodeBase64ToArray(base64) {
  return JSON.parse(Buffer.from(base64, 'base64').toString());
}

export function encodeSetToBase64(set) {
  return encodeArrayToBase64(Array.from(set));
}

export function decodeBase64ToSet(base64) {
  return new Set(decodeBase64ToArray(base64));
}
