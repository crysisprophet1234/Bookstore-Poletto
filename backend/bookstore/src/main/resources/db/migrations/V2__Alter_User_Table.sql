-- Column: public.bookstore_user.verified_account

-- ALTER TABLE IF EXISTS public.bookstore_user DROP COLUMN IF EXISTS verified_account;

ALTER TABLE IF EXISTS public.bookstore_user
    ADD COLUMN verified_account boolean NOT NULL DEFAULT false;