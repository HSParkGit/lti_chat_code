import * as z from 'zod';
import { passwordSchema } from '../../libs/zod';

export const useUserValidationSchema = (t) => {
  return z.object({
    name: z.string().min(1, { message: t('validation.name_required') }),
    birth: z.string().min(1, { message: t('validation.birth_required') }),
    mobile: z.object({
      phoneNumber: z.string(),
      countryCode: z.string(),
    }),
    email: z.string().email({ message: t('validation.invalid_email') }),
    // department: z.string().min(1, { message: t('validation.department_required') }),
  });
};

export const useUserPasswordValidationSchema = (t, isCurrentUser) => {
  return z
    .object({
      oldPassword: isCurrentUser ? z.string().min(1, 'Current password is required') : z.string().optional(),
      newPassword: z.string().min(6, 'Old password must be at least 6 characters long'),
      checkNewPassword: z.string().min(6, 'New password must be at least 6 characters long'),
    })
    .refine((data) => data.newPassword === data.checkNewPassword, {
      message: "Passwords don't match",
      path: ['checkNewPassword'],
    });
};

export const useAddUserFormValidationSchema = (t) => {
  return z.object({
    name: z
      .string({
        required_error: t('form_error.required', { field: t('modal.add_user.name') }),
      })
      .min(4, t('form_error.required', { field: t('modal.add_user.name') })),
    user_number: z
      .string({
        required_error: t('form_error.required', { field: t('modal.add_user.user_number') }),
      })
      .min(4, t('form_error.required', { field: t('modal.add_user.user_number') })),
    password: passwordSchema,
    email: z
      .string({
        required_error: t('form_error.required', { field: t('modal.add_user.email') }),
      })
      .email(t('form_error.required', { field: t('modal.add_user.email') })),
    birth: z
      .string({
        required_error: t('form_error.required', { field: t('modal.add_user.birth') }),
      })
      .min(4, t('form_error.required', { field: t('modal.add_user.birth') })),
    mobile: z
      .object({
        countryCode: z.string(),
        phoneNumber: z.string(),
      })
      .refine((val) => val.countryCode.trim() !== '' || val.phoneNumber.trim() !== '', {
        message: t('form_error.required', { field: t('modal.add_user.mobile') }),
      }),
    role: z
      .string({
        required_error: t('form_error.required', { field: t('modal.add_user.role') }),
      })
      .min(4, t('form_error.required', { field: t('modal.add_user.role') })),
  });
};
