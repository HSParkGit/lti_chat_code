export class ApiError extends Error {
  // need to implement later
  constructor(message) {
    super(message);
    this.message = message;
    this.name = 'ApiError';
  }
}
