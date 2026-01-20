import { t } from 'i18next';
import { z } from 'zod';

export const passwordSchema = z.string().min(8, { message: t('modal.add_user.strong_password') });
